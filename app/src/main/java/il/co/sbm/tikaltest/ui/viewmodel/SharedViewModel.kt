package il.co.sbm.tikaltest.ui.viewmodel

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import il.co.sbm.tikaltest.model.network.objects.TmdbMovieObject

class SharedViewModel : ViewModel() {

    val mSelectedMovie: MutableLiveData<TmdbMovieObject> = MutableLiveData()

    fun selectMovie(iSelectedMovie: TmdbMovieObject) {

        mSelectedMovie.value = iSelectedMovie
    }
}