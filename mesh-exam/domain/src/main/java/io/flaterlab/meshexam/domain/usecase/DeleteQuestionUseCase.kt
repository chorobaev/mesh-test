package io.flaterlab.meshexam.domain.usecase

import io.flaterlab.meshexam.domain.datasource.ExamDataSource
import javax.inject.Inject

class DeleteQuestionUseCase @Inject constructor(
    private val examDataSource: ExamDataSource,
) {

    suspend operator fun invoke(vararg questionIds: String) {
        examDataSource.deleteQuestions(*questionIds)
    }
}