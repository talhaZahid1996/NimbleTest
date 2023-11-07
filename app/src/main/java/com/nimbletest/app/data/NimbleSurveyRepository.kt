package com.nimbletest.app.data

import androidx.room.withTransaction
import com.nimbletest.app.data.api.NetworkResponse
import com.nimbletest.app.data.api.NimbleSurveyApiService
import com.nimbletest.app.data.database.NimbleSurveyDataBase
import com.nimbletest.app.data.database.entities.SurveyEntity
import com.nimbletest.app.data.result.Result
import com.nimbletest.app.util.toSurveyEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

interface NimbleSurveyRepository {
    fun getSurveys(): Flow<List<SurveyEntity>>
    suspend fun getSurveysFromNetwork(): Result<Unit, Unit>
}

class NimbleSurveyRepositoryImpl @Inject constructor(
    private val apiService: NimbleSurveyApiService,
    private val dataBase: NimbleSurveyDataBase
) : NimbleSurveyRepository {
    override fun getSurveys(): Flow<List<SurveyEntity>> {
        return dataBase.surveyDao().getSurveys()
    }

    override suspend fun getSurveysFromNetwork(): Result<Unit, Unit> {
        val randomPageSize = (6..15).random() // simulate daily data flow
        val result = apiService.getSurveyList(
            page = 1,
            pageSize = randomPageSize
        )
        return when (result) {
            is NetworkResponse.Success -> {
                val surveys = result.body.data?.map { it.toSurveyEntity() }
                dataBase.withTransaction {
                    surveys?.let {
                        dataBase.surveyDao().clearAllSurveys()
                        dataBase.surveyDao().insertSurveys(surveys)
                    }
                }
                Result.Success(Unit)
            }

            else -> {
                Result.Error(Unit)
            }
        }
    }
}