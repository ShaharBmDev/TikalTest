package il.co.sbm.tikaltest.ui.view.fragments

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.Toast
import il.co.sbm.tikaltest.R
import il.co.sbm.tikaltest.model.global.TikalTestApplication
import il.co.sbm.tikaltest.model.network.objects.TmdbMovieObject
import il.co.sbm.tikaltest.model.repositories.Resource
import il.co.sbm.tikaltest.ui.view.adapters.MoviesAdapter
import il.co.sbm.tikaltest.ui.viewmodel.MoviesListViewModel
import il.co.sbm.tikaltest.ui.viewmodel.SharedViewModel

class MoviesListFragment : Fragment() {

    companion object {
        fun newInstance() = MoviesListFragment()

        private const val MOVIES_COLUMNS: Int = 2
    }

    private lateinit var mViewModel: MoviesListViewModel
    private lateinit var mSharedViewModel: SharedViewModel
    private var mMoviesAdapter: MoviesAdapter? = null
    private lateinit var mRvMovies: RecyclerView
    private lateinit var mPbLoading: ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initSharedViewModelIfNeeded()
    }

    private fun initSharedViewModelIfNeeded() {
        if (TikalTestApplication.mInstance.mIsTablet) {
            mSharedViewModel = activity?.run {
                ViewModelProviders.of(this).get(SharedViewModel::class.java)
            } ?: throw Exception(getString(R.string.invalid_activity))
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.movies_list_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        initViewModel()
    }

    private fun initViewModel() {

        mViewModel = ViewModelProviders.of(this).get(MoviesListViewModel::class.java)
        mViewModel.mMoviesList.observe(this, Observer {
            //update ui
            if (it != null) {
                when (it.status) {
                    Resource.Status.SUCCESS -> {
                        setAdapter()
                    }
                    Resource.Status.ERROR -> {
                        Toast.makeText(activity, "ERROR: ${it.message}", Toast.LENGTH_LONG).show()
                    }
                    Resource.Status.LOADING -> {
                        showProgressBar(true)
                    }

                }
                showProgressBar(false)
            }
        })
    }

    private fun showProgressBar(iShouldShow: Boolean) {
        mPbLoading.visibility = if (iShouldShow) View.VISIBLE else View.GONE
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initUiReferences(view)
        showProgressBar(true)
    }

    private fun initUiReferences(view: View) {

        mRvMovies = view.findViewById(R.id.rv_moviesList_movies)
        mPbLoading = view.findViewById(R.id.pb_moviesList_loading)
    }

    private fun setAdapter() {

        if (mMoviesAdapter == null) {
            mRvMovies.layoutManager = GridLayoutManager(this.context, MOVIES_COLUMNS)
            mMoviesAdapter = MoviesAdapter(this.context!!, mViewModel.mMoviesList) { clickedMovie: TmdbMovieObject -> onMovieItemClick(clickedMovie) }
            mRvMovies.adapter = mMoviesAdapter
        } else {
            mMoviesAdapter!!.notifyDataSetChanged()
        }
    }

    private fun onMovieItemClick(iClickedMovie: TmdbMovieObject) {
        if (TikalTestApplication.mInstance.mIsTablet) {
            mSharedViewModel.selectMovie(iClickedMovie)
        } else {
            openMovieDetailsFragment(iClickedMovie)
        }
    }

    private fun openMovieDetailsFragment(iClickedMovie: TmdbMovieObject) {
        activity?.supportFragmentManager?.beginTransaction()
            ?.add(R.id.container, MovieDetailsFragment.newInstance(iClickedMovie))
            ?.addToBackStack(null)?.commit()
    }
}
