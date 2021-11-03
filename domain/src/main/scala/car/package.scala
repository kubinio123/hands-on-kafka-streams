package object car {

  case class CarId(value: Int)

  case class CarSpeed(value: Int)
  case class CarEngine(rpm: Int)
  case class CarLocation(locationId: LocationId)

  case class DriverNotification(msg: String)

  case class LocationId(city: String, street: String)
  case class LocationData(speedLimit: Int, trafficVolume: TrafficVolume.Value)

  object TrafficVolume extends Enumeration {
    val Low, Medium, High = Value
  }
}
