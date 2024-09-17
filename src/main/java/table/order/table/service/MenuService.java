package table.order.table.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import table.order.table.dto.MenuDTO;
import table.order.table.exception.InvalidUserDataException;
import table.order.table.model.Menu;
import table.order.table.repository.MenuRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class MenuService {

    @Autowired
    private MenuRepository menuRepository;

    // Create a new Menu item
    public MenuDTO createMenu(MenuDTO menuDTO) {
        if (menuDTO.getName() == null || menuDTO.getName().isEmpty()) {
            throw new InvalidUserDataException("Menu name cannot be null or empty");
        }

        if (menuDTO.getPrice() == null || menuDTO.getPrice() <= 0) {
            throw new InvalidUserDataException("Price must be greater than 0");
        }

        Menu menu = new Menu();
        menu.setName(menuDTO.getName());
        menu.setPrice(menuDTO.getPrice());
        menu.setDescription(menuDTO.getDescription());

        Menu savedMenu = menuRepository.save(menu);
        return new MenuDTO(savedMenu.getId(), savedMenu.getName(), savedMenu.getPrice(), savedMenu.getDescription());
    }

    // Get Menu item by ID
    public MenuDTO getMenuById(Long id) {
        if (id == null) {
            throw new InvalidUserDataException("Menu ID cannot be null");
        }

        Menu menu = menuRepository.findById(id)
                .orElseThrow(() -> new InvalidUserDataException("Menu item not found with ID: " + id));

        return new MenuDTO(menu.getId(), menu.getName(), menu.getPrice(), menu.getDescription());
    }


    // Get all Menu items
    public List<MenuDTO> getAllMenus() {
        List<Menu> menus = menuRepository.findAll();

        return menus.stream()
                .map(menu -> new MenuDTO(menu.getId(), menu.getName(), menu.getPrice(), menu.getDescription()))
                .collect(Collectors.toList());
    }

    // Update an existing Menu item
    public MenuDTO updateMenu(Long id, MenuDTO menuDTO) {
        if (id == null) {
            throw new InvalidUserDataException("Menu ID cannot be null");
        }

        Menu existingMenu = menuRepository.findById(id)
                .orElseThrow(() -> new InvalidUserDataException("Menu item not found with ID: " + id));

        if (menuDTO.getName() != null && !menuDTO.getName().isEmpty()) {
            existingMenu.setName(menuDTO.getName());
        }

        if (menuDTO.getPrice() != null && menuDTO.getPrice() > 0) {
            existingMenu.setPrice(menuDTO.getPrice());
        }

        if (menuDTO.getDescription() != null && !menuDTO.getDescription().isEmpty()) {
            existingMenu.setDescription(menuDTO.getDescription());
        }

        Menu updatedMenu = menuRepository.save(existingMenu);
        return new MenuDTO(updatedMenu.getId(), updatedMenu.getName(), updatedMenu.getPrice(), updatedMenu.getDescription());
    }

    // Delete a Menu item
    public void deleteMenu(Long id) {
        if (id == null) {
            throw new InvalidUserDataException("Menu ID cannot be null");
        }

        if (menuRepository.existsById(id)) {
            menuRepository.deleteById(id);
        } else {
            throw new InvalidUserDataException("Menu item not found with ID: " + id);
        }
    }



}
