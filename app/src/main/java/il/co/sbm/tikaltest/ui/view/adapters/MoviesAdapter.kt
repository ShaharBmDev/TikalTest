package il.co.sbm.tikaltest.ui.view.adapters

import android.arch.lifecycle.LiveData
import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import com.squareup.picasso.Picasso
import il.co.sbm.tikaltest.R
import il.co.sbm.tikaltest.model.network.core.RequestStringBuilder
import il.co.sbm.tikaltest.model.network.objects.TmdbMovieObject
import il.co.sbm.tikaltest.model.repositories.Resource
import kotlinx.android.synthetic.main.cell_movie.view.*

class MoviesAdapter(
    private val mContext: Context, private var mMoviesList: LiveData<Resource<List<TmdbMovieObject>>>, private val mOnMovieItemClickListener: (TmdbMovieObject) -> Unit
) : RecyclerView.Adapter<MoviesAdapter.ViewHolder>() {

    override fun onCreateViewHolder(iParent: ViewGroup, iViewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(mContext).inflate(
                R.layout.cell_movie,
                iParent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return mMoviesList.value?.data?.size ?: 0
    }

    override fun onBindViewHolder(iViewHolder: ViewHolder, iPosition: Int) {

        val currentMovie: TmdbMovieObject = mMoviesList.value?.data?.get(iPosition) ?: TmdbMovieObject()

        Picasso.get().load(RequestStringBuilder.getPosterUrl(currentMovie.mPposterPath))
            .placeholder(R.drawable.movie_default_background)
            .error(R.drawable.movie_default_background).fit().centerCrop().into(iViewHolder.mIvPoster)
        iViewHolder.mTvReleaseYear.text =
                if (currentMovie.mReleaseDate.length >= 4) currentMovie.mReleaseDate.substring(0, 4) else ""
        iViewHolder.mTvTitle.text = currentMovie.mTitle
        iViewHolder.mRtbRating.rating = (currentMovie.mVoteAverage / 2).toFloat()
        iViewHolder.mTvRating.text = mContext.getString(R.string.out_of_ten_rating, currentMovie.mVoteAverage)

        iViewHolder.itemView.setOnClickListener { mOnMovieItemClickListener(currentMovie) }
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val mIvPoster: ImageView = itemView.iv_cellMovie_poster
        val mTvReleaseYear: TextView = itemView.tv_cellMovie_releaseYear
        val mTvTitle: TextView = itemView.tv_cellMovie_title
        val mRtbRating: RatingBar = itemView.rtb_cellMovie_rating
        val mTvRating: TextView = itemView.tv_cellMovie_rating
    }
}