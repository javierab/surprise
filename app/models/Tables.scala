package models
// AUTO-GENERATED Slick data model
/** Stand-alone Slick data model for immediate use */
object Tables extends Tables {
  val profile = slick.jdbc.MySQLProfile
}

/** Slick data model trait for extension, choice of backend or usage in the cake pattern. (Make sure to initialize this late.) */
trait Tables {
  val profile: slick.jdbc.JdbcProfile
  import profile.api._
  import slick.model.ForeignKeyAction
  // NOTE: GetResult mappers for plain SQL are only generated for tables where Slick knows how to map the types of all columns.
  import slick.jdbc.{GetResult => GR}

  /** DDL for all tables. Call .create to execute. */
  lazy val schema: profile.SchemaDescription = Message.schema ++ User.schema ++ UserFile.schema
  @deprecated("Use .schema instead of .ddl", "3.0")
  def ddl = schema

  /** Entity class storing rows of table Message
   *  @param id Database column id SqlType(INT)
   *  @param fid Database column fid SqlType(INT)
   *  @param message Database column message SqlType(LONGTEXT), Length(2147483647,true), Default(None)
   *  @param `private` Database column private SqlType(BIT), Default(false) */
  case class MessageRow(id: Int, fid: Int, message: Option[String] = None, `private`: Boolean = false)
  /** GetResult implicit for fetching MessageRow objects using plain SQL queries */
  implicit def GetResultMessageRow(implicit e0: GR[Int], e1: GR[Option[String]], e2: GR[Boolean]): GR[MessageRow] = GR{
    prs => import prs._
    MessageRow.tupled((<<[Int], <<[Int], <<?[String], <<[Boolean]))
  }
  /** Table description of table message. Objects of this class serve as prototypes for rows in queries.
   *  NOTE: The following names collided with Scala keywords and were escaped: private */
  class Message(_tableTag: Tag) extends profile.api.Table[MessageRow](_tableTag, Some("surprise"), "message") {
    def * = (id, fid, message, `private`) <> (MessageRow.tupled, MessageRow.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = ((Rep.Some(id), Rep.Some(fid), message, Rep.Some(`private`))).shaped.<>({r=>import r._; _1.map(_=> MessageRow.tupled((_1.get, _2.get, _3, _4.get)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))

    /** Database column id SqlType(INT) */
    val id: Rep[Int] = column[Int]("id")
    /** Database column fid SqlType(INT) */
    val fid: Rep[Int] = column[Int]("fid")
    /** Database column message SqlType(LONGTEXT), Length(2147483647,true), Default(None) */
    val message: Rep[Option[String]] = column[Option[String]]("message", O.Length(2147483647,varying=true), O.Default(None))
    /** Database column private SqlType(BIT), Default(false)
     *  NOTE: The name was escaped because it collided with a Scala keyword. */
    val `private`: Rep[Boolean] = column[Boolean]("private", O.Default(false))

    /** Foreign key referencing User (database name message_ibfk_1) */
    lazy val userFk = foreignKey("message_ibfk_1", id, User)(r => r.id, onUpdate=ForeignKeyAction.Restrict, onDelete=ForeignKeyAction.Restrict)
    /** Foreign key referencing UserFile (database name message_ibfk_2) */
    lazy val userFileFk = foreignKey("message_ibfk_2", fid, UserFile)(r => r.id, onUpdate=ForeignKeyAction.Restrict, onDelete=ForeignKeyAction.Restrict)
  }
  /** Collection-like TableQuery object for table Message */
  lazy val Message = new TableQuery(tag => new Message(tag))

  /** Entity class storing rows of table User
   *  @param id Database column id SqlType(INT), AutoInc, PrimaryKey
   *  @param name Database column name SqlType(VARCHAR), Length(300,true)
   *  @param email Database column email SqlType(VARCHAR), Length(100,true) */
  case class UserRow(id: Int, name: String, email: String)
  /** GetResult implicit for fetching UserRow objects using plain SQL queries */
  implicit def GetResultUserRow(implicit e0: GR[Int], e1: GR[String]): GR[UserRow] = GR{
    prs => import prs._
    UserRow.tupled((<<[Int], <<[String], <<[String]))
  }
  /** Table description of table user. Objects of this class serve as prototypes for rows in queries. */
  class User(_tableTag: Tag) extends profile.api.Table[UserRow](_tableTag, Some("surprise"), "user") {
    def * = (id, name, email) <> (UserRow.tupled, UserRow.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = ((Rep.Some(id), Rep.Some(name), Rep.Some(email))).shaped.<>({r=>import r._; _1.map(_=> UserRow.tupled((_1.get, _2.get, _3.get)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))

    /** Database column id SqlType(INT), AutoInc, PrimaryKey */
    val id: Rep[Int] = column[Int]("id", O.AutoInc, O.PrimaryKey)
    /** Database column name SqlType(VARCHAR), Length(300,true) */
    val name: Rep[String] = column[String]("name", O.Length(300,varying=true))
    /** Database column email SqlType(VARCHAR), Length(100,true) */
    val email: Rep[String] = column[String]("email", O.Length(100,varying=true))
  }
  /** Collection-like TableQuery object for table User */
  lazy val User = new TableQuery(tag => new User(tag))

  /** Entity class storing rows of table UserFile
   *  @param id Database column id SqlType(INT), AutoInc, PrimaryKey
   *  @param userId Database column user_id SqlType(INT)
   *  @param filename Database column filename SqlType(VARCHAR), Length(300,true) */
  case class UserFileRow(id: Int, userId: Int, filename: String)
  /** GetResult implicit for fetching UserFileRow objects using plain SQL queries */
  implicit def GetResultUserFileRow(implicit e0: GR[Int], e1: GR[String]): GR[UserFileRow] = GR{
    prs => import prs._
    UserFileRow.tupled((<<[Int], <<[Int], <<[String]))
  }
  /** Table description of table user_file. Objects of this class serve as prototypes for rows in queries. */
  class UserFile(_tableTag: Tag) extends profile.api.Table[UserFileRow](_tableTag, Some("surprise"), "user_file") {
    def * = (id, userId, filename) <> (UserFileRow.tupled, UserFileRow.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = ((Rep.Some(id), Rep.Some(userId), Rep.Some(filename))).shaped.<>({r=>import r._; _1.map(_=> UserFileRow.tupled((_1.get, _2.get, _3.get)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))

    /** Database column id SqlType(INT), AutoInc, PrimaryKey */
    val id: Rep[Int] = column[Int]("id", O.AutoInc, O.PrimaryKey)
    /** Database column user_id SqlType(INT) */
    val userId: Rep[Int] = column[Int]("user_id")
    /** Database column filename SqlType(VARCHAR), Length(300,true) */
    val filename: Rep[String] = column[String]("filename", O.Length(300,varying=true))

    /** Foreign key referencing User (database name user_file_ibfk_1) */
    lazy val userFk = foreignKey("user_file_ibfk_1", userId, User)(r => r.id, onUpdate=ForeignKeyAction.Restrict, onDelete=ForeignKeyAction.Restrict)
  }
  /** Collection-like TableQuery object for table UserFile */
  lazy val UserFile = new TableQuery(tag => new UserFile(tag))
}
