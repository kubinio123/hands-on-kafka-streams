package car.drivernotifier

import car.domain.{DriverNotification, LocationData, TrafficVolume}
import car.drivernotifier.DriverNotifierData.{CarAndLocationData, CarData}

object DriverNotifications {

  def apply(data: CarAndLocationData): List[DriverNotification] =
    List(checkSpeed, checkTrafficVolume, checkEngineRPM, checkFuelLevel).flatten(_.lift(data))

  private val checkSpeed: PartialFunction[CarAndLocationData, DriverNotification] = {
    case CarAndLocationData(CarData(Some(speed), _, _), LocationData(speedLimit, _, _)) if speed.value > speedLimit =>
      DriverNotification(s"Slow down, speed limit $speedLimit")
  }

  private val checkTrafficVolume: PartialFunction[CarAndLocationData, DriverNotification] = {
    case CarAndLocationData(CarData(_, _, Some(location)), LocationData(_, TrafficVolume.High, _)) =>
      DriverNotification(s"High traffic ahead on ${location.locationId.street} street")
  }

  private val checkEngineRPM: PartialFunction[CarAndLocationData, DriverNotification] = {
    case CarAndLocationData(CarData(_, Some(engine), _), _) if engine.rpm > HighRPM => DriverNotification("Shift up a gear")
  }

  private val checkFuelLevel: PartialFunction[CarAndLocationData, DriverNotification] = {
    case CarAndLocationData(CarData(_, Some(engine), _), LocationData(_, _, gasStationNearby))
        if engine.fuelLevel <= FuelReserve && gasStationNearby =>
      DriverNotification("Low fuel level, navigate to nearest gas station?")
  }

  private val HighRPM = 3000
  private val FuelReserve = 0.2
}
