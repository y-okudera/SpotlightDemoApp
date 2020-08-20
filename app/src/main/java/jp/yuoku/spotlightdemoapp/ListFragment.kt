package jp.yuoku.spotlightdemoapp

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.DecelerateInterpolator
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModelProviders
import com.takusemba.spotlight.OnSpotlightListener
import com.takusemba.spotlight.OnTargetListener
import com.takusemba.spotlight.Spotlight

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
            showSpotlight(inflater, container, "評価をしてください")
        }
        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(ListViewModel::class.java)
        // TODO: Use the ViewModel
    }

    private fun showSpotlight(inflater: LayoutInflater, container: ViewGroup?, text: String) {
        Log.d("DEBUG", "showSpotlight")

        val activity = this.activity ?: return
        val context = this.context ?: return

        val coachmarkRoot = FrameLayout(context)
        val coachmarkView = layoutInflater.inflate(R.layout.layout_coachmark, coachmarkRoot)

        val target = makeSpotlightTarget(activity, coachmarkView, text)
        val spotlight = makeSpotlight(activity, target)

        // 閉じるボタンの処理登録
        val closeButton = coachmarkView.findViewById<Button>(R.id.close_spotlight)
        closeButton.setOnClickListener { spotlight.finish() }

        // スポットライト開始
        spotlight.start()
    }

    private fun makeSpotlightTarget(activity: FragmentActivity, coachmarkView: View, text: String): com.takusemba.spotlight.Target {
        Log.d("DEBUG", "makeSpotlightTarget")

        val targetRatingBar = activity.findViewById<View>(R.id.ratingBar)

        return com.takusemba.spotlight.Target.Builder()
            // スポットライトを当てるviewを設定
            .setAnchor(targetRatingBar)
            .setShape(
                com.takusemba.spotlight.shape.RoundedRectangle(
                    targetRatingBar.height.toFloat(),
                    targetRatingBar.width.toFloat(),
                    16.toFloat()
                )
            )
            .setOverlay(coachmarkView)
            .setOnTargetListener(object : OnTargetListener {
                override fun onStarted() {
                    Log.d("DEBUG", "Target onStarted")
                    activity.findViewById<TextView>(R.id.custom_text).text = text
                }

                override fun onEnded() {
                    Log.d("DEBUG", "Target onEnded")
                }
            })
            .build()
    }

    private fun makeSpotlight(activity: FragmentActivity, target: com.takusemba.spotlight.Target): Spotlight {
        Log.d("DEBUG", "makeSpotlight")

        return Spotlight.Builder(activity)
            .setTargets(target)
            .setBackgroundColor(R.color.spotlight)
            .setDuration(1000L)
            .setAnimation(DecelerateInterpolator(2f))
            .setOnSpotlightListener(object : OnSpotlightListener {
                override fun onStarted() {
                    Log.d("DEBUG", "Spotlight onStarted")
                }

                override fun onEnded() {
                    Log.d("DEBUG", "Spotlight onEnded")
                }
            })
            .build()
    }
}