package com.example

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.example.data.local.AppDatabase
import com.example.data.repository.SuggestionRepository
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [34])
class ExampleRobolectricTest {

    @Test
    fun `read string from context`() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        val appName = context.getString(R.string.app_name)
        assertEquals("Political Science 2nd Paper Suggestion Pro", appName)
    }

    @Test
    fun `initialize database and seed built-in questions`() = runBlocking {
        val context = ApplicationProvider.getApplicationContext<Context>()
        val db = AppDatabase.getDatabase(context)
        val repository = SuggestionRepository(db)
        repository.ensureInitialized()

        val list = repository.allQuestions.first()
        assertTrue("Database should have seeded built-in questions", list.isNotEmpty())

        val stats = repository.getStats()
        assertTrue(stats.totalQuestions > 0)
        assertNotNull(stats)
    }
}
