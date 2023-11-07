package com.nimbletest.app.util

import com.nimbletest.app.data.database.entities.SurveyEntity
import com.nimbletest.app.data.models.Survey

fun Survey.toSurveyEntity(): SurveyEntity {
    return SurveyEntity(
        title = this.attributes.title,
        description = this.attributes.description,
        coverImageUrl = this.attributes.coverImageUrl,
        createdAt = this.attributes.createdAt
    )
}