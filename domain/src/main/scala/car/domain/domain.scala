package car

package object domain {

  case class CarId(value: Int)

  case class CarSpeed(value: Int)
  case class CarEngine(rpm: Int, fuelLevel: Double)
  case class CarLocation(locationId: LocationId)

  case class DriverNotification(msg: String)

  case class LocationId(city: String, street: String)
  case class LocationData(speedLimit: Int, trafficVolume: TrafficVolume.Value, gasStationNearby: Boolean)

  object TrafficVolume extends Enumeration {
    val Low, Medium, High = Value
  }
}
