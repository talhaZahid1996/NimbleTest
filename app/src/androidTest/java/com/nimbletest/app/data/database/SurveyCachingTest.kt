package com.nimbletest.app.data.database

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import com.nimbletest.app.data.database.dao.SurveyDao
import com.nimbletest.app.data.database.entities.SurveyEntity
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@SmallTest
class SurveyCachingTest {

    private lateinit var database: NimbleSurveyDataBase
    private lateinit var surveyDao: SurveyDao

    @Before
    fun setup() {
        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            NimbleSurveyDataBase::class.java
        ).allowMainThreadQueries().build()

        surveyDao = database.surveyDao()
    }

    @After
    fun teardown() {
        database.close()
    }

    @Test
    fun testInsertAndGetSurveyEntity() = runTest {
        val surveyEntity = SurveyEntity(
            title = "Survey 1",
            description = "Survey 1 description",
            coverImageUrl = "https://www.google.com",
            createdAt = "2021-09-01T00:00:00.000Z",
        )
        val surveyEntityList = listOf(surveyEntity)

        surveyDao.insertSurveys(surveyEntityList)

        val surveyEntityFromDB = surveyDao.getSurveys().first()

        assert(surveyEntityFromDB.isNotEmpty())
    }

    @Test
    fun testInsertAndGetDataIntegrity() = runTest {
        val surveyEntity = SurveyEntity(
            title = "Survey 2",
            description = "Survey 2 description",
            coverImageUrl = "https://www.google.com/2",
            createdAt = "2021-09-01T00:00:00.000Z",
        )
        val surveyEntityList = listOf(surveyEntity)

        surveyDao.insertSurveys(surveyEntityList)

        val surveyEntityFromDB = surveyDao.getSurveys().first()

        assert(surveyEntityFromDB.first().title == surveyEntity.title)
    }

    @Test
    fun testClearSurveys() = runTest {
        val surveyEntity = SurveyEntity(
            title = "Survey 3",
            description = "Survey 3 description",
            coverImageUrl = "https://www.google.com/3",
            createdAt = "2021-09-01T00:00:00.000Z",
        )
        val surveyEntityList = listOf(surveyEntity)

        surveyDao.insertSurveys(surveyEntityList)

        surveyDao.clearAllSurveys()

        val surveyEntityFromDB = surveyDao.getSurveys().first()

        assert(surveyEntityFromDB.isEmpty())
    }

    @Test
    fun testCorrectInsertion() = runTest {
        val surveyEntity = SurveyEntity(
            title = "Survey 4",
            description = "Survey 4 description",
            coverImageUrl = "https://www.google.com/4",
            createdAt = "2021-09-01T00:00:00.000Z",
        )
        val surveyEntityList = listOf(surveyEntity)

        surveyDao.insertSurveys(surveyEntityList)

        val surveyEntityUpdated = SurveyEntity(
            title = "Survey 4 Updated",
            description = "Survey 4 description Updated",
            coverImageUrl = "https://www.google.com/4/updated",
            createdAt = "2021-09-01T00:00:00.000Z",
        )
        val surveyEntityListUpdated = listOf(surveyEntityUpdated)

        surveyDao.insertSurveys(surveyEntityListUpdated)

        val surveyEntityFromDBUpdated = surveyDao.getSurveys().first()

        assert(surveyEntityFromDBUpdated.first().title != surveyEntityUpdated.title)
    }

    @Test
    fun testOnConflictStrategyWorks() = runTest {
        val surveyEntity = SurveyEntity(
            title = "Survey 5",
            description = "Survey 5 description",
            coverImageUrl = "https://www.google.com/5",
            createdAt = "2021-09-01T00:00:00.000Z",
        )
        val surveyEntityList = listOf(surveyEntity)

        surveyDao.insertSurveys(surveyEntityList)

        val surveyEntityUpdated = SurveyEntity(
            title = "Survey 5",
            description = "Survey 5 description Updated",
            coverImageUrl = "https://www.google.com/5",
            createdAt = "2021-09-01T00:00:00.000Z",
        )
        val surveyEntityListUpdated = listOf(surveyEntityUpdated)

        surveyDao.insertSurveys(surveyEntityListUpdated)

        val surveyEntityFromDBUpdated = surveyDao.getSurveys().first()

        assert(surveyEntityFromDBUpdated.first().title == surveyEntityUpdated.title)
        assert(surveyEntityFromDBUpdated.first().description != surveyEntityUpdated.description)
    }
}