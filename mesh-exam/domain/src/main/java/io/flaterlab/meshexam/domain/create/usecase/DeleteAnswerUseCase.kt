package io.flaterlab.meshexam.domain.create.usecase

import io.flaterlab.meshexam.domain.datasource.ExamDataSource
import javax.inject.Inject

class DeleteAnswerUseCase @Inject constructor(
    private val examDataSource: ExamDataSource,
) {

    suspend operator fun invoke(vararg answerIds: String) {
        examDataSource.deleteAnswers(*answerIds)
    }
}