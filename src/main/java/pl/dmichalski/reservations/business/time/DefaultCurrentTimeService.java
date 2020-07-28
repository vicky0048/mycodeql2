package pl.dmichalski.reservations.business.time;

import java.util.Date;

import org.springframework.stereotype.Service;

@Service
class DefaultCurrentTimeService implements CurrentTimeService {

    @Override
    public Date getCurrentDate() {
        return new Date();
    }

}
