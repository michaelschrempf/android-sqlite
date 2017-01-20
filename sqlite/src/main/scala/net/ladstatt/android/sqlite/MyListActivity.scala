package net.ladstatt.android.sqlite

import android.app.ListActivity
import android.os.Bundle
import android.provider.Contacts.People
import android.view.View
import android.widget.{ArrayAdapter, ListView, SimpleCursorAdapter}

import scala.collection.JavaConversions._

/**
  * Created by lad on 19/01/2017.
  */
class MyListActivity extends ListActivity {

  var aDb: SimpleDb = _

  override def onCreate(savedInstanceState: Bundle): Unit = {
    super.onCreate(savedInstanceState)

    aDb = SimpleDb(getApplicationContext)

    val persons: List[Person] = aDb.mkPersonDao().findByFirstName("ggg")

    //val ps : List[Person] = List(Person("aaa","b"))

    val pA = new ArrayAdapter[Person](this, android.R.layout.simple_list_item_1, persons)

    setListAdapter(pA)
  }

  override def onListItemClick(l: ListView, v: View, pos: Int, i: Long) {
    println("Pos: " + pos + " clicked")
    val p  = l.getAdapter.getItem(pos).asInstanceOf[Person]
    println(p)
  }
}