package models

final case class UserProfile (id: Long, firstName: String, lastName: String,
                              email: String)

case class UserProfileData (firstName: String, lastName: String,
                              email: String)