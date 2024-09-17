package table.order.table.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import table.order.table.dto.MenuDTO;
import table.order.table.exception.InvalidUserDataException;
import table.order.table.service.MenuService;

import java.util.List;

@RestController
@RequestMapping("/menus")
public class MenuController {
    @Autowired
    private MenuService menuService;

    @PostMapping
    public MenuDTO createMenu(@RequestBody MenuDTO menuDTO) {
        return menuService.createMenu(menuDTO);
    }

    @GetMapping
    public List<MenuDTO> getAllMenus() {
        return menuService.getAllMenus();
    }

    @GetMapping("/{id}")
    public MenuDTO getMenuById(@PathVariable Long id) {
        MenuDTO menuDTO = menuService.getMenuById(id);
        if (menuDTO == null) {
            throw new InvalidUserDataException("Menu not found with ID: " + id);
        }
        return menuDTO;
    }

    @PutMapping("/{id}")
    public MenuDTO updateMenu(@PathVariable Long id, @RequestBody MenuDTO menuDTO) {
        MenuDTO existingMenuDTO = menuService.getMenuById(id);
        if (existingMenuDTO == null) {
            throw new InvalidUserDataException("Menu not found with ID: " + id);
        }
        return menuService.updateMenu(id, menuDTO);
    }

    @DeleteMapping("/{id}")
    public void deleteMenu(@PathVariable Long id) {
        MenuDTO menuDTO = menuService.getMenuById(id);
        if (menuDTO == null) {
            throw new InvalidUserDataException("Menu not found with ID: " + id);
        }
        menuService.deleteMenu(id);
    }

}
