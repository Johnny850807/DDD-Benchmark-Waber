package tw.waterball.ddd.waber.user.usecases;

import lombok.AllArgsConstructor;
import tw.waterball.ddd.commons.exceptions.NotFoundException;
import tw.waterball.ddd.model.user.Activity;
import tw.waterball.ddd.model.user.Driver;
import tw.waterball.ddd.waber.passenger.repositories.ActivityRepository;

import javax.inject.Named;
import java.util.Collection;
import java.util.stream.Collectors;

/**
 * @author - johnny850807@gmail.com (Waterball)
 */
@Named
@AllArgsConstructor
public class QueryDrivers {
    private ActivityRepository activityRepository;

    public Collection<Driver> execute(Request req) {
        Activity activity = activityRepository.findByName(req.activityName)
                .orElseThrow(NotFoundException::new);
        return activity.getParticipantDrivers().stream()
                .filter(driver -> driver.getCarType() == req.carType)
                .collect(Collectors.toList());
    }

    @AllArgsConstructor
    public static class Request {
        public String activityName;
        public Driver.CarType carType;
    }
}