package com.example.walkaid.view.activity

import android.content.Context
import com.example.walkaid.data.WalkAidDB

object GlobalContext {

   fun getDatabaseInstance(context: Context): WalkAidDB {
      return WalkAidDB.getInstance(context)
   }
}