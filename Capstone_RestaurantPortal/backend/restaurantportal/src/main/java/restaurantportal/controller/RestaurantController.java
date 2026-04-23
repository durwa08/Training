package restaurantportal.controller;

import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;
import restaurantportal.dto.RestaurantRequest;
import restaurantportal.dto.RestaurantResponse;
import restaurantportal.service.RestaurantService;

import java.util.List;

@RestController
@RequestMapping("/api/restaurants")
// RestaurantController to handle restaurant related endpoints like create restaurant get all restaurants get restaurant by id update restaurant delete restaurant etc.
public class RestaurantController {

    private final RestaurantService service;

    public RestaurantController(RestaurantService service) {
        this.service = service;
    }

    //  CREATE
    @PostMapping
    public RestaurantResponse create(@Valid @RequestBody RestaurantRequest request) {
        return service.create(request);
    }

    // GET ALL
    @GetMapping
    public List<RestaurantResponse> getAll() {
        return service.getAll();
    }

    //  GET BY ID
    @GetMapping("/{id}")
    public RestaurantResponse getById(@PathVariable Long id) {
        return service.getById(id);
    }

    // UPDATE
    @PutMapping("/{id}")
    public RestaurantResponse update(@PathVariable Long id,
                                     @RequestBody RestaurantRequest request) {
        return service.update(id, request);
    }

    //  DELETE
    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }
}