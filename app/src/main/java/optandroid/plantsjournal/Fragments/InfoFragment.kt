package optandroid.plantsjournal.Fragments

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.MediaController
import android.widget.VideoView
import optandroid.plantsjournal.R

class InfoFragment : Fragment() {

    var simpleVideoView: VideoView? = null

    // declaring a null variable for MediaController
    var mediaControls: MediaController? = null
    private var videoURL = "https://youtu.be/ygvnPllrQW8"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view: View =  inflater.inflate(R.layout.fragment_info, container, false)

        simpleVideoView = view.findViewById<View>(R.id.simpleVideoView) as VideoView
        var videoShareButton = view.findViewById<Button>(R.id.shareVideoButton)
        //val videoShareButton = view.findViewById<Button>(R.id.shareVideoButton)

        videoShareButton.setOnClickListener{
            val intent = Intent(Intent.ACTION_SEND).apply {

                putExtra(Intent.EXTRA_TEXT, videoURL)
                putExtra(Intent.EXTRA_TITLE, "Share PlantsJournal.org mission")
                type = "text/plain"
            }
            val shareIntent = Intent.createChooser(intent, null)
            startActivity(shareIntent)
        }

        if (mediaControls == null) {
            mediaControls = MediaController(activity)
            mediaControls!!.setAnchorView(simpleVideoView)
        }

        // set the media controller for video view
        simpleVideoView!!.setMediaController(mediaControls)
        // set the absolute path of the video file which is going to be played
        simpleVideoView!!.setVideoURI(Uri.parse("android.resource://"
                + requireActivity().packageName + "/" + R.raw.video
        ))
        simpleVideoView!!.requestFocus()
        // starting the video
        simpleVideoView!!.start()

        return view
    }

}