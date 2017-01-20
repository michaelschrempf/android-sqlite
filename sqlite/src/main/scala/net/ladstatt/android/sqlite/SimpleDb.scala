package net.ladstatt.android.sqlite

import android.content.{ContentValues, Context, CursorLoader}
import android.database.Cursor
import android.database.sqlite.{SQLiteDatabase, SQLiteOpenHelper}

import scala.collection.mutable.ListBuffer

object SimpleDb {

  val Name = "mydb"
}

/**
  * A simple wrapper around SQLiteOpenHelper to ease SQLite access.
  *
  * Created by lad on 19/01/2017.
  */
case class SimpleDb(context: Context) extends SQLiteOpenHelper(context, SimpleDb.Name, null, 1) {

  override def onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int): Unit = ()

  override def onCreate(db: SQLiteDatabase): Unit = {

    // perform initial setup
    val personDao = SqlitePersonDao(db)

    personDao.init()

    for (i <- 1 to 100) personDao.insert(Person.mkRandom)

  }

  def mkPersonDao(): SqlitePersonDao = SqlitePersonDao(getWritableDatabase)


  trait BaseDao[T] {

    def insert(t: T): Long

    def update(t: T): Int

    // ... and other functions
  }

  def mkContentValues(p: Person): ContentValues = {
    val cv = new ContentValues
    cv.put("firstname", p.firstName)
    cv.put("secondname", p.secondName)
    cv
  }

  /**
    * Hides details of database table 'Person'
    *
    * @param db
    */
  case class SqlitePersonDao(db: SQLiteDatabase) extends BaseDao[Person] {

    def init(): Unit = db.execSQL("create table person (id INTEGER PRIMARY KEY ASC, firstname TEXT, secondname TEXT);")

    /**
      * Insert a person to the database.
      *
      * @param p
      */
    def insert(p: Person): Long = {
      val cv: ContentValues = mkContentValues(p)
      db.insert("person", null, cv)
    }

    def deleteByFirstName(firstName: String): Unit = {
      db.delete("person", "firstname = ?", Array(firstName))
    }

    def update(p: Person): Int = {
      db.update("person", mkContentValues(p), "firstname = ? and secondname = ?", Array(p.firstName, p.secondName))
    }

    /**
      * Returns a list of persons matching given firstName, or Nil if there is none
      *
      * @param firstName the firstName to search for
      * @return
      */
    def findByFirstName(firstName: String): List[Person] = {
      var someCursor: Option[Cursor] = None
      try {
        someCursor = someCursorForFirstnameQuery(firstName)
        someCursor match {
          case None =>
            System.err.println("Could not execute query due to some reason")
            Nil
          case Some(c) =>
            val lb = new ListBuffer[Person]()
            while (c.moveToNext()) {
              val id = c.getInt(c.getColumnIndex("id"))
              val firstName = c.getString(c.getColumnIndex("firstname"))
              val secondName = c.getString(c.getColumnIndex("secondname"))
              lb.append(Person(firstName, secondName))
            }
            lb.toList
        }
      } finally {
        someCursor foreach (_.close())
      }

    }

    /**
      * Returns an optional cursor for a firstname query on the person table.
      *
      * @param firstName
      * @return
      */
    private def someCursorForFirstnameQuery(firstName: String): Option[Cursor] = {
      Option(db.query("person", Array("id", "firstname", "secondname"), "firstname = ?", Array(firstName), null, null, null))
    }

    private def somePersonCursor(): Option[Cursor] = {
      Option(db.query("person", Array("id", "firstname", "secondname"), null, null, null, null, null))
    }


  }


}