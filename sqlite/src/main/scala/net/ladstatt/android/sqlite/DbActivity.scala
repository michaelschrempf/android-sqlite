package net.ladstatt.android.sqlite

import android.app.Activity
import android.content.{ContentValues, Context, Intent}
import android.database.Cursor
import android.database.sqlite.{SQLiteDatabase, SQLiteOpenHelper}
import android.os.Bundle
import android.text.Editable
import android.view.View
import android.widget.EditText

import scala.collection.JavaConversions._
import scala.util.Random


/**
  * Shows how to lookup user interface elements via findViewById, and more importantly
  * an example how to use SQLiteOpenHelper class
  */
class DbActivity extends Activity {

  var aDb: SimpleDb = _

  override protected def onCreate(savedInstanceState: Bundle) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.first)
    aDb = SimpleDb(getApplicationContext)
  }

  def saveToDb(view: View): Unit = {
    val firstName: String = findViewById(R.id.firstName).asInstanceOf[EditText].getText.toString
    val secondName: String = findViewById(R.id.secondName).asInstanceOf[EditText].getText.toString

    // I WANT TO WRITE TO THE DATABASE
    aDb.mkPersonTable().insert(Person(firstName, secondName))
  }

  def loadFromDb(view: View): Unit = {
    val i = new Intent(this, classOf[MyListActivity])
    startActivity(i)

  }
}
