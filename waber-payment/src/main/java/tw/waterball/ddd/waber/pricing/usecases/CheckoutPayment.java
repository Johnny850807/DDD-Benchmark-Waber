package tw.waterball.ddd.waber.pricing.usecases;

import lombok.AllArgsConstructor;
import tw.waterball.ddd.api.match.MatchServiceDriver;
import tw.waterball.ddd.api.match.MatchView;
import tw.waterball.ddd.api.trip.TripServiceDriver;
import tw.waterball.ddd.api.trip.TripView;
import tw.waterball.ddd.model.payment.Payment;
import tw.waterball.ddd.model.payment.PricingStrategy;
import tw.waterball.ddd.model.trip.Trip;

import javax.inject.Named;

/**
 * @author Waterball (johnny850807@gmail.com)
 */
@Named
@AllArgsConstructor
public class CheckoutPayment {
    private PricingStrategy pricingStrategy;
    private MatchServiceDriver matchServiceDriver;
    private TripServiceDriver tripServiceDriver;

    public void execute(Request request, Presenter presenter) {
        Trip trip = getTrip(request);
        Payment payment = new Payment(pricingStrategy);
        payment.checkout(trip);
        presenter.present(payment);
    }

    private Trip getTrip(Request request) {
        MatchView matchView = matchServiceDriver.getMatch(request.passengerId, request.matchId);
        TripView tripView = tripServiceDriver.getTrip(request.tripId);
        return tripView.toEntity(matchView.toEntity());
    }

    @AllArgsConstructor
    public static class Request {
        public int passengerId, matchId;
        public String tripId;
    }

    public interface Presenter {
        void present(Payment payment);
    }
}