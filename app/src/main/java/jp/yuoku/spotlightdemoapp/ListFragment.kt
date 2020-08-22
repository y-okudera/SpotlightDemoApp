package jp.yuoku.spotlightdemoapp

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.DecelerateInterpolator
import android.widget.Button
import android.widget.FrameLayout
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.takusemba.spotlight.Spotlight
import com.takusemba.spotlight.Target
import com.takusemba.spotlight.shape.RoundedRectangle
import kotlinx.android.synthetic.main.layout_coachmark.view.*
import kotlinx.android.synthetic.main.list_fragment.*


class ListFragment : Fragment() {

    companion object {
        fun newInstance() = ListFragment()
    }

    private lateinit var viewModel: ListViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.list_fragment, container, false)

        val startButton = view.findViewById<Button>(R.id.start_button)
        startButton?.setOnClickListener {
            showSpotlight(R.string.spotlight_message)
        }
        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(ListViewModel::class.java)
        // TODO: Use the ViewModel
    }

    private fun showSpotlight(@StringRes spotlightMessageId: Int) {
        Log.d("DEBUG", "showSpotlight")

        val coachmarkRoot = FrameLayout(requireContext())
        val coachMarkView = layoutInflater.inflate(R.layout.layout_coachmark, coachmarkRoot)
        coachMarkView.spotlight_text.setText(spotlightMessageId)

        val target = makeSpotlightTarget(ratingBar, coachMarkView)
        val spotlight = makeSpotlight(target)

        // 閉じるボタンの処理登録
        coachMarkView.close_spotlight.setOnClickListener { spotlight.finish() }

        // coachMarkViewのボタン以外の部分タップでも閉じれるようにする場合、 xmlのandroid:clickable="true" android:focusable="true"　を消す必要がある
        // その場合は、スポットライトを当てている部分のタップが有効になる（スポットライト表示中にratingViewを操作できてしまう）
//        coachMarkView.setOnTouchListener { v, event ->
//            Log.d("TEST", "setOnTouchListener")
//            spotlight.finish()
//            false
//        }

        // スポットライト開始
        spotlight.start()
    }

    /**
     * SpotlightTargetを生成
     *
     * @param[targetView] スポットライトを当てる対象のview
     * @param[coachMarkView] スポットライト用のview
     * @return SpotlightTarget
     */
    private fun makeSpotlightTarget(targetView: View, coachMarkView: View): Target {

        return Target.Builder()
            .setAnchor(targetView)
            .setShape(RoundedRectangle(targetView.height.toFloat(), targetView.width.toFloat(), 16.toFloat()))
            .setOverlay(coachMarkView)
            .build()
    }

    /**
     * Spotlightを生成
     *
     * @param[target] スポットライトを当てる対象
     * @return Spotlight
     */
    private fun makeSpotlight(target: Target): Spotlight {

        return Spotlight.Builder(requireActivity())
            .setTargets(target)
            .setBackgroundColor(R.color.spotlight)
            .setDuration(1000L)
            .setAnimation(DecelerateInterpolator(2f))
            .build()
    }
}
