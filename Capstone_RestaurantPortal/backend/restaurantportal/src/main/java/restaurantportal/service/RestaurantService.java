package restaurantportal.service;
import org.springframework.stereotype.Service;
import restaurantportal.entity.Restaurant;
import restaurantportal.repository.RestaurantRepository;
import java.util.*;
@Service//Contains business logic
public class RestaurantService {
    //Adding Dependencies;
    private final RestaurantRepository restaurantRepository;
    public RestaurantService(RestaurantRepository restaurantRepository) {
        this.restaurantRepository = restaurantRepository;
    }
    //Create Restaurant
    public Restaurant create(Restaurant restaurant) {
        return restaurantRepository.save(restaurant);
    }

    //Get all restaurant
    public List<Restaurant> getAll() {
        return restaurantRepository.findAll();
    }

    //get by id
    public Restaurant getById(Long id){
        return restaurantRepository.findById(id).orElseThrow(() -> new RuntimeException("Restaurant not found"));
    }
    //Update
    public Restaurant update(Long id,Restaurant updated){
        Restaurant r = getById(id);

        r.setName(updated.getName());
        r.setAddress(updated.getAddress());
        r.setStatus(updated.getStatus());
        return restaurantRepository.save(r);
    }

    //delete by id
    public void delete(Long id){
        restaurantRepository.deleteById(id);
    }


}