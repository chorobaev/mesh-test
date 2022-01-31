package io.flaterlab.meshexam.create.ui.question

import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.flaterlab.meshexam.androidbase.BaseViewModel
import io.flaterlab.meshexam.androidbase.SingleLiveEvent
import io.flaterlab.meshexam.androidbase.getLauncher
import io.flaterlab.meshexam.androidbase.text.Text
import io.flaterlab.meshexam.create.R
import io.flaterlab.meshexam.create.dvo.QuestionMetaDvo
import io.flaterlab.meshexam.domain.create.model.CreateQuestionModel
import io.flaterlab.meshexam.domain.create.usecase.CreateQuestionUseCase
import io.flaterlab.meshexam.domain.create.usecase.DeleteQuestionUseCase
import io.flaterlab.meshexam.domain.create.usecase.GetExamUseCase
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
internal class CreateQuestionViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val getExamUseCase: GetExamUseCase,
    private val createQuestionUseCase: CreateQuestionUseCase,
    private val deleteQuestionUseCase: DeleteQuestionUseCase,
) : BaseViewModel() {

    private val launcher: CreateQuestionLauncher = savedStateHandle.getLauncher()

    private val _questionMetaInfo = MutableLiveData<QuestionMetaDvo>()
    val questionMetaInfo: LiveData<QuestionMetaDvo> = _questionMetaInfo

    private val _questionIds = MutableLiveData<List<String>>(emptyList())
    val questionIds: LiveData<List<String>> = _questionIds

    val actionEnabled = MutableLiveData(launcher.actionBehavior != null)
    val actionTitleResId = MutableLiveData(launcher.actionBehavior?.actionTitleResId)

    val questionIdAddedCommand = SingleLiveEvent<String>()
    val applyNavActionCommand = SingleLiveEvent<(Fragment) -> Unit>()

    init {
        loadExam()
    }

    fun loadExam() {
        viewModelScope.launch {
            getExamUseCase(launcher.examId).apply {
                _questionMetaInfo.value = QuestionMetaDvo(exam.name, exam.type, exam.durationInMin)
                _questionIds.value = questionIds
            }
        }
    }

    fun onAddQuestionClicked() {
        viewModelScope.launch {
            val ids = questionIds.value!!
            val newQuestionId = createQuestionUseCase(
                CreateQuestionModel(
                    examId = launcher.examId,
                    orderNumber = ids.size,
                    score = 1F,
                )
            )
            _questionIds.value = ids + newQuestionId
            questionIdAddedCommand.value = newQuestionId
        }
    }

    fun onDeleteQuestionAt(position: Int) {
        viewModelScope.launch {
            val ids = questionIds.value!!
            deleteQuestionUseCase(ids[position])
            _questionIds.value = ids.toMutableList().apply {
                removeAt(position)
            }
            message.value =
                Text.from(R.string.create_create_question_questionAtIntDeleted, position + 1)
        }
    }

    fun onActionClicked() {
        val behavior = launcher.actionBehavior ?: return
        applyNavActionCommand.value = behavior::onActionClicked
    }
}