package net.ladstatt.android.sqlite

import android.app.Activity
import android.content.{ContentValues, Context}
import android.database.Cursor
import android.database.sqlite.{SQLiteDatabase, SQLiteOpenHelper}
import android.os.Bundle
import android.text.Editable
import android.view.View
import android.widget.EditText

import scala.collection.JavaConversions._

case class FooDb(context: Context) extends SQLiteOpenHelper(context, "mydb", null, 1) {

  override def onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int): Unit = ()

  override def onCreate(db: SQLiteDatabase): Unit = {
    db.execSQL("create table person (id INTEGER PRIMARY KEY ASC, firstname TEXT);")
  }
}

/**
  * Shows how to lookup user interface elements via findViewById, and more importantly
  * an example how to use SQLiteOpenHelper class
  */
class FirstActivity extends Activity {

  var fooDb: FooDb = _

  override protected def onCreate(savedInstanceState: Bundle) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.first)
    fooDb = FooDb(getApplicationContext)
  }

  def saveToDb(view: View): Unit = {
    val firstNameEditText = findViewById(R.id.firstName).asInstanceOf[EditText]
    val firstName: String = firstNameEditText.getText.toString
    println(firstName)

    // I WANT TO WRITE TO THE DATABASE

    val cv = new ContentValues()
    Map("firstname" -> firstName) foreach {
      case (k, v) => cv.put(k, v)
    }
    fooDb.getWritableDatabase().insert("person", null, cv)


    // I WANT TO READ THE DATABASE

    var someCursor: Option[Cursor] = None
    try {
      someCursor = Option(fooDb.getReadableDatabase.query("person", Array("id", "firstname"), null, null, null, null, null))

      someCursor match {
        case None => System.err.println("Could not execute query due to some reason")
        case Some(c) =>
          while (c.moveToNext()) {
            val id = c.getInt(c.getColumnIndex("id"))
            val firstName = c.getString(c.getColumnIndex("firstname"))
            println(s"ID($id) : $firstName")
          }
      }
    } finally {
      someCursor foreach (_.close())
    }
  }

}
