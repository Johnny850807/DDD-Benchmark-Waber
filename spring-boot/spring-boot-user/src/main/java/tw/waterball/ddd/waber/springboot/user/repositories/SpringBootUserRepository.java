package tw.waterball.ddd.waber.springboot.user.repositories;

import org.springframework.stereotype.Component;
import tw.waterball.ddd.commons.exceptions.NotFoundException;
import tw.waterball.ddd.model.associations.One;
import tw.waterball.ddd.model.geo.Location;
import tw.waterball.ddd.model.user.User;
import tw.waterball.ddd.waber.springboot.user.repositories.jpa.JpaUserDataPort;
import tw.waterball.ddd.waber.springboot.user.repositories.jpa.UserData;
import tw.waterball.ddd.waber.user.repositories.UserRepository;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author - johnny850807@gmail.com (Waterball)
 */
@Component
public class SpringBootUserRepository implements UserRepository {
    private JpaUserDataPort jpaUserDataPort;

    public SpringBootUserRepository(JpaUserDataPort jpaUserDataPort) {
        this.jpaUserDataPort = jpaUserDataPort;
    }

    @Override
    public Optional<User> findById(int userId) {
        return jpaUserDataPort.findById(userId)
                .map(UserData::toEntity);
    }

    @Override
    public User save(User user) {
        UserData data = jpaUserDataPort.save(UserData.fromEntity(user));
        user.setId(data.getId());
        return user;
    }

    @Override
    public List<User> findAllByIds(Iterable<Integer> userIds) {
        return jpaUserDataPort.findAllById(userIds)
                .stream().map(UserData::toEntity)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<User> findByEmailAndPassword(String email, String password) {
        return jpaUserDataPort.findByEmailAndPassword(email, password)
                .map(UserData::toEntity);
    }

    @Override
    public void updateLatestLocation(int userId, Location location) {
        UserData data = jpaUserDataPort.getOne(userId);
        data.setLatitude(location.getLatitude());
        data.setLongitude(location.getLongitude());
        jpaUserDataPort.save(data);
    }

    @Override
    public User associateById(int id) {
        try {
            return jpaUserDataPort.getOne(id).toEntity();
        } catch (EntityNotFoundException err) {
            throw new NotFoundException(err);
        }
    }
}
