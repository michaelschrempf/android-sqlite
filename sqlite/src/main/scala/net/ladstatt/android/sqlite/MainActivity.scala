package net.ladstatt.android.sqlite

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.View.OnClickListener
import android.widget.Button

/**
  * Entry point of the appliation.
  *
  * Shows how to use Intents to change between Activities
  */
class MainActivity extends Activity {


  override protected def onCreate(savedInstanceState: Bundle) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.main)
  }

  // called by a button click - see main.xml
  def gotoFirstActivity(view: View): Unit = {
    val i = new Intent(this, classOf[DbActivity])
    startActivity(i)
  }

}
