package tw.waterball.ddd.waber.springboot.trip.repositories.jpa;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import tw.waterball.ddd.model.geo.Location;
import tw.waterball.ddd.model.match.Match;
import tw.waterball.ddd.model.trip.Trip;
import tw.waterball.ddd.model.trip.TripState;
import tw.waterball.ddd.model.trip.TripStateType;


/**
 * @author Waterball (johnny850807@gmail.com)
 */
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Document("trip")
public class TripData {
    @Id
    public String id;
    public int matchId;
    public int driverId;
    public int passengerId;
    public Location destination;
    public TripStateType state;

    public TripData(String id, int driverId, int passengerId, int matchId, Location destination,
                    TripState state) {
        this.id = id;
        this.driverId = driverId;
        this.passengerId = passengerId;
        this.matchId = matchId;
        this.destination = destination;
        this.state = state.getType();
    }

    public static TripData toData(Trip trip, Match match) {
        int driverId = match.getDriverId();
        int passengerId = match.getPassengerId();
        return new TripData(trip.getId(), driverId, passengerId, match.getId(),
                trip.getDestination(), trip.getState());
    }

    public Trip toEntity() {
        return new Trip(id, matchId, destination, state);
    }
}
