package il.co.sbm.tikaltest.ui.viewmodel

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.ViewModel
import il.co.sbm.tikaltest.model.network.objects.TmdbMovieObject
import il.co.sbm.tikaltest.model.repositories.Resource
import il.co.sbm.tikaltest.model.repositories.TmdbMoviesRepository

class MoviesListViewModel : ViewModel() {

    var mMoviesList: LiveData<Resource<List<TmdbMovieObject>>> = TmdbMoviesRepository.loadMovies()
        private set
}
