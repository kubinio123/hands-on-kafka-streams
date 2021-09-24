package object car {

  case class CarDataKey(carId: Int)

  case class CarSpeedData(speed: Int)
  case class CarEngineData(rpm: Int)
  case class CarLocationData(city: String, street: String)
  case class DriverNotification(msg: String)

  case class LocationDataKey(city: String, street: String)
  case class LocationData(speedLimit: Int, trafficVolume: TrafficVolume.Value)

  object TrafficVolume extends Enumeration {
    val Low, Medium, High = Value
  }
}
