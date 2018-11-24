package il.co.sbm.tikaltest.ui.viewmodel

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import il.co.sbm.tikaltest.model.network.objects.response.TmdbGetMovieExternalIdsResponse
import il.co.sbm.tikaltest.model.network.objects.response.TmdbGetMovieVideosResponse
import il.co.sbm.tikaltest.model.repositories.TmdbMoviesRepository

class MovieDetailsViewModel : ViewModel() {

    var mMovieExternalIdsResponse: MutableLiveData<TmdbGetMovieExternalIdsResponse> = TmdbMoviesRepository.getMovieExternalIds(null)
    var mMovieVideos: MutableLiveData<TmdbGetMovieVideosResponse> = TmdbMoviesRepository.getMovieVideos(null)

    fun getMovieExternalIds(iMovieId: Int?) {
        TmdbMoviesRepository.getMovieExternalIds(iMovieId)
    }

    fun getMovieVideos(iMovieId: Int?) {
        TmdbMoviesRepository.getMovieVideos(iMovieId)
    }
}
