package il.co.sbm.tikaltest.ui.view.fragments

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.app.Fragment
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.squareup.picasso.Picasso
import il.co.sbm.tikaltest.R
import il.co.sbm.tikaltest.model.global.AppConsts
import il.co.sbm.tikaltest.model.global.TikalTestApplication
import il.co.sbm.tikaltest.model.network.core.RequestStringBuilder
import il.co.sbm.tikaltest.model.network.objects.TmdbMovieObject
import il.co.sbm.tikaltest.model.network.objects.TmdbMovieVideo
import il.co.sbm.tikaltest.ui.viewmodel.MovieDetailsViewModel
import il.co.sbm.tikaltest.ui.viewmodel.SharedViewModel
import il.co.sbm.tikaltest.utils.AppUtils
import kotlinx.android.synthetic.main.movie_details_fragment.*

class MovieDetailsFragment : Fragment() {

    companion object {

        private const val KEY_SELECTED_MOVIE = "selectedMovie"

        fun newInstance() = MovieDetailsFragment()

        fun newInstance(iSelectedMovie: TmdbMovieObject): MovieDetailsFragment {
            val fragment = MovieDetailsFragment()

            val bundle = Bundle()
            bundle.putParcelable(KEY_SELECTED_MOVIE, iSelectedMovie)
            fragment.arguments = bundle

            return fragment
        }
    }

    private lateinit var mViewModel: MovieDetailsViewModel
    private lateinit var mSharedViewModel: SharedViewModel
    private val mSelectedMovie: TmdbMovieObject? by lazy {
        arguments?.getParcelable<TmdbMovieObject>(KEY_SELECTED_MOVIE)
    }
    private var mImdbId: String? = null
    private var mMovieVideoObject: TmdbMovieVideo? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initViewModel()
        initSharedViewModelIfNeeded()
    }

    private fun initSharedViewModelIfNeeded() {
        if (TikalTestApplication.mInstance.mIsTablet) {
            mSharedViewModel = activity?.run {
                ViewModelProviders.of(this).get(SharedViewModel::class.java)
            } ?: throw Exception(getString(R.string.invalid_activity))

            mSharedViewModel.mSelectedMovie.observe(this, Observer<TmdbMovieObject> { iSelectedMovie ->
                // Update the UI
                updateUi(iSelectedMovie)
            })
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.movie_details_fragment, container, false)
    }

    private fun initViewModel() {

        mViewModel = ViewModelProviders.of(this).get(MovieDetailsViewModel::class.java)
        mViewModel.mMovieExternalIdsResponse.observe(this, Observer {
            if (it != null && !TextUtils.isEmpty(it.imdbId)) {
                mImdbId = it.imdbId
                updateImdButton()
            }
        })

        mViewModel.mMovieVideos.observe(this, Observer {
            mMovieVideoObject = it?.results?.get(0)
            updateVideoButton()
        })
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (mSelectedMovie != null) {
            updateUi(mSelectedMovie)
        }

        setListeners()
    }

    private fun setListeners() {
        ib_movieDetails_imdb.setOnClickListener {
            if (!TextUtils.isEmpty(mImdbId)) {
                startActivity(AppUtils.openUrlIntent(RequestStringBuilder.getImdbMovieUrl(mImdbId!!)))
            }
        }

        ib_movieDetails_youtube.setOnClickListener {
            if (isVideoValid()) {
                activity?.let { it1 -> AppUtils.watchYoutubeVideo(it1, mMovieVideoObject?.key!!) }
            }
        }
    }

    private fun updateUi(iSelectedMovie: TmdbMovieObject?) {

        updateImdButton()
        updateVideoButton()

        iSelectedMovie?.mId?.let { mViewModel.getMovieExternalIds(it) }
        iSelectedMovie?.mId?.let { mViewModel.getMovieVideos(it) }
        Picasso.get().load(RequestStringBuilder.getPosterUrl(iSelectedMovie?.mPposterPath ?: ""))
            .placeholder(R.drawable.movie_default_background)
            .error(R.drawable.movie_default_background).fit().centerCrop().into(iv_movieDetails_poster)
        tv_movieDetails_releaseDate.text = if (iSelectedMovie?.mReleaseDate?.length ?: 0 >= 4) iSelectedMovie?.mReleaseDate?.substring(0, 4) else ""
        tv_movieDetails_title.text = iSelectedMovie?.mTitle ?: ""
        rtb_movieDetails_rating.rating = ((iSelectedMovie?.mVoteAverage ?: 0.0) / 2).toFloat()
        tv_movieDetails_rating.text = getString(R.string.out_of_ten_rating, iSelectedMovie?.mVoteAverage ?: 0.0)
        tv_movieDetails_overview.text = iSelectedMovie?.mOverview ?: ""

        hideNoMovieSelectedCover()
    }

    private fun updateImdButton() {
        ib_movieDetails_imdb.visibility = if (TextUtils.isEmpty(mImdbId)) View.GONE else View.VISIBLE
    }

    private fun updateVideoButton() {
        ib_movieDetails_youtube.visibility = if (!isVideoValid()) View.GONE else View.VISIBLE
    }

    private fun isVideoValid(): Boolean {
        return mMovieVideoObject != null && mMovieVideoObject?.site?.toLowerCase().equals(AppConsts.YOUTUBE) && !TextUtils.isEmpty(mMovieVideoObject?.key)
    }

    private fun hideNoMovieSelectedCover() {
        tv_movieDetails_NoneSelectedCover.visibility = View.GONE
    }
}
