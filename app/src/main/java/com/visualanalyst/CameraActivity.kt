/* Copyright 2017 The TensorFlow Authors. All Rights Reserved.

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
==============================================================================*/
package com.visualanalyst

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

/** Main `Activity` class for the Camera app.  */
class CameraActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_camera)

        val newFragment = Camera2BasicFragment()
        val transaction = supportFragmentManager.beginTransaction()

        transaction.replace(R.id.container, newFragment, "settings_selected")
        transaction.addToBackStack("settings_selected")
            .commit()
    }

    override fun onBackPressed() {
        finish()
    }
}