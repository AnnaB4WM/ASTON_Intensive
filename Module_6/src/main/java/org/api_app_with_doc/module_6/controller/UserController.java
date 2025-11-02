package org.api_app_with_doc.module_6.controller;

import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.api_app_with_doc.module_6.dto.UserDTO;
import org.api_app_with_doc.module_6.services.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.CollectionModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import java.util.List;

@RestController
@RequestMapping("/users")
@Tag(name = "User-service API", description = "API для управления пользователями")
public class UserController {

    private final UserServiceImpl userService;

    @Autowired
    public UserController(UserServiceImpl userService) {
        this.userService = userService;
    }

    @Operation(summary = "Получение всех пользователей",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Список всех пользователей",
                            content = @Content(schema = @Schema(implementation = UserDTO.class))),
                    @ApiResponse(responseCode = "404", description = "Пользователи не найдены")
            })
    @GetMapping
    public ResponseEntity<CollectionModel<UserDTO>> getAllUsers() {
        List<UserDTO> users = userService.getAllUsers()
                .stream()
                .peek(this::addLinks)
                .toList();
        return ResponseEntity.ok(CollectionModel.of(users,
                linkTo(methodOn(UserController.class).getAllUsers()).withSelfRel()));
    }

    @Operation(summary = "Получение пользователя по id",
            parameters = @Parameter(name = "id", description = "ID пользователя"),
            responses = {
                    @ApiResponse(responseCode = "200", description = "Пользователь найден",
                            content = @Content(schema = @Schema(implementation = UserDTO.class))),
                    @ApiResponse(responseCode = "404", description = "Пользователь не найден")
            })
    @GetMapping("/{id}")
    public ResponseEntity<UserDTO> getUserById(@PathVariable Long id) {
        UserDTO dto = userService.getUserById(id);
        addLinks(dto);
        return ResponseEntity.ok(dto);
    }

    @Operation(summary = "Создание пользователя")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Пользователь создан"),
            @ApiResponse(responseCode = "400", description = "Неверные данные запроса")
    })
    @PostMapping
    public ResponseEntity<UserDTO> createUser(@RequestBody UserDTO userDTO) {
        userService.createUser(userDTO);
        return ResponseEntity.status(201).build();
    }

    @Operation(summary = "Обновление пользователя")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Пользователь успешно обновлён"),
            @ApiResponse(responseCode = "404", description = "Пользователь не найден"),
            @ApiResponse(responseCode = "400", description = "Неверные данные пользователя")
    })
    @PutMapping("/{id}")
    public ResponseEntity<UserDTO> updateUser(@PathVariable Long id, @RequestBody UserDTO userDTO) {
        userService.updateUser(id, userDTO);
        return ResponseEntity.notFound().build();
    }

    @Operation(summary = "Удаление пользователя",
            parameters = @Parameter(name = "id", description = "ID пользователя"),
            responses = {
            @ApiResponse(responseCode = "204", description = "Пользователь успешно удалён"),
            @ApiResponse(responseCode = "404", description = "Пользователь не найден")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<UserDTO> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.notFound().build();
    }

    private void addLinks(UserDTO dto) {
        if (dto.getId() == null) return;
        Link self = linkTo(methodOn(UserController.class).getUserById(dto.getId())).withSelfRel();
        Link all = linkTo(methodOn(UserController.class).getAllUsers()).withRel("all");
        Link update = linkTo(methodOn(UserController.class).updateUser(dto.getId(), dto)).withRel("update");
        Link delete = linkTo(methodOn(UserController.class).deleteUser(dto.getId())).withRel("delete");
        dto.add(self);
        dto.add(all);
        dto.add(update);
        dto.add(delete);
    }
}
