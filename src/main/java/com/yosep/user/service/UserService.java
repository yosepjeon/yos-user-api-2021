package com.yosep.user.service;

import com.yosep.user.common.KeyCloakUtil;
import com.yosep.user.data.dto.request.UserDtoForCreation;
import com.yosep.user.data.dto.response.CreatedUserDto;
import com.yosep.user.data.entity.User;
import com.yosep.user.data.mapper.UserMapper;
import com.yosep.user.mq.producer.UserToProductProducer;
import com.yosep.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.resource.UserResource;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.concurrent.ExecutionException;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final Keycloak keycloak;
    private final UserToProductProducer userToProductProducer;

    public boolean checkDuplicatedUser(String userId) {
        return userRepository.findById(userId).isPresent() ? true : false;
    }

    @Transactional(readOnly = false)
    public Boolean createUser(UserDtoForCreation userDtoForCreation)
            throws ExecutionException, InterruptedException {
        User user = UserMapper.INSTANCE.userDtoForCreationToUser(userDtoForCreation);

        if (userRepository.findById(user.getUserId()).isPresent()) {

            return false;
        }

        User createdUser = userRepository.save(user);
        CreatedUserDto createdUserDto = UserMapper.INSTANCE.userToCreatedUserDto(createdUser);


        CredentialRepresentation credential = new CredentialRepresentation();
        credential.setType(CredentialRepresentation.PASSWORD);
        credential.setValue(createdUser.getPassword());
        UserRepresentation keycloakUser = new UserRepresentation();
        keycloakUser.setUsername(createdUserDto.getUserId());
        keycloakUser.setCredentials(Arrays.asList(credential));

        javax.ws.rs.core.Response response = keycloak
                .realm("yosep")
                .users()
                .create(keycloakUser);
        final int status = response.getStatus();

        final String createdId = KeyCloakUtil.getCreatedId(response);
        // Reset password
        CredentialRepresentation newCredential = new CredentialRepresentation();
        UserResource userResource = keycloak.realm("yosep").users().get(createdId);
        newCredential.setType(CredentialRepresentation.PASSWORD);
        newCredential.setValue(createdUser.getPassword());
        newCredential.setTemporary(false);
        userResource.resetPassword(newCredential);

        userToProductProducer.produceCartCreation(user.getUserId());

        return true;
    }
}
