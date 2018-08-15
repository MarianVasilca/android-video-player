package marian.vasilca.videoplayer.ui.fragments

import android.Manifest
import android.arch.lifecycle.ViewModelProviders
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.support.annotation.RequiresApi
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import androidx.navigation.findNavController
import io.reactivex.rxkotlin.plusAssign
import kotlinx.android.synthetic.main.fragment_local_files.*
import marian.vasilca.videoplayer.R
import marian.vasilca.videoplayer.data.model.VideoFile
import marian.vasilca.videoplayer.databinding.FragmentLocalFilesBinding
import marian.vasilca.videoplayer.di.InjectorUtils
import marian.vasilca.videoplayer.ui.BaseFragment
import marian.vasilca.videoplayer.ui.adapters.videofiles.VideoFilesAdapter
import marian.vasilca.videoplayer.ui.common.AlertDialogFragment
import marian.vasilca.videoplayer.ui.common.BaseDialogFragment
import marian.vasilca.videoplayer.utilities.autoCleared
import marian.vasilca.videoplayer.utilities.hasPermission
import marian.vasilca.videoplayer.viewmodels.files.FilesViewModel

/**
 * A fragment representing the start destination of the application.
 */
open class LocalFilesFragment : BaseFragment<FragmentLocalFilesBinding>(),
        BaseDialogFragment.OnDialogItemSelectedListener {
    override val layoutResource: Int
        get() = R.layout.fragment_local_files

    override val tag: String
        get() = "LocalFilesFragment"

    protected var adapter by autoCleared<VideoFilesAdapter>()
    private lateinit var viewModel: FilesViewModel
    private val localFiles: ArrayList<VideoFile> = ArrayList()

    override fun onBoundViews(savedInstanceState: Bundle?) {
        val factory = InjectorUtils.provideFilesViewModelFactory(requireContext().applicationContext)
        viewModel = ViewModelProviders.of(this, factory).get(FilesViewModel::class.java)
        setAdapter()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            checkReadPermission()
        } else {
            getVideoFiles()
        }
    }

    private fun setAdapter() {
        adapter = VideoFilesAdapter { _, position ->
            // accept clicks only for LocalFilesFragment instance
            if (this@LocalFilesFragment is CreatePlaylistFragment) return@VideoFilesAdapter

            val bundle = Bundle()
            bundle.putParcelableArrayList(VideoPlayerFragment.PLAYLIST_TAG, localFiles)
            bundle.putInt(VideoPlayerFragment.CURRENT_ITEM_TAG, position)
            view?.findNavController()?.navigate(R.id.video_player_action, bundle)
        }

        rvLocalFiles.apply {
            setHasFixedSize(true)
            itemAnimator = DefaultItemAnimator()
            addItemDecoration(DividerItemDecoration(
                    this@LocalFilesFragment.requireContext(),
                    LinearLayoutManager.VERTICAL)
            )
            adapter = this@LocalFilesFragment.adapter
        }
    }

    private fun getVideoFiles() {
        compositeDisposable += viewModel.observeFiles(onNext = { list ->
            localFiles.clear()
            localFiles.addAll(list)
            adapter.submitList(list)
            onCreateSelectionTracker(videoFiles = localFiles)
        })
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun checkReadPermission() {
        Manifest.permission.READ_EXTERNAL_STORAGE.hasPermission(requireContext()).also {
            when (it) {
                true -> getVideoFiles()
                false -> requestReadExternalPermission()
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun requestReadExternalPermission() {
        if (requireActivity().shouldShowRequestPermissionRationale(Manifest.permission.READ_EXTERNAL_STORAGE)) {
            showReadExternalPermissionDialog()
        } else {
            onContinue()
        }
    }

    private fun showReadExternalPermissionDialog() {
        val alertDialogFragment = AlertDialogFragment.create(
                id = READ_PERMISSION_CODE,
                title = R.string.read_permission_title,
                message = R.string.read_permission_text,
                positiveButton = R.string.cont,
                negativeButton = R.string.cancel
        )
        alertDialogFragment.show(childFragmentManager, READ_PERMISSION_DIALOG_TAG)
    }

    override fun onPositiveButtonSelected(id: Int) {
        when (id) {
            READ_PERMISSION_CODE -> {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    onContinue()
                }
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun onContinue() {
        requestPermissions(arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                READ_PERMISSION_CODE)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == READ_PERMISSION_CODE) {
            if (grantResults.size == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getVideoFiles()
            } else {
                // handle permission not granted here
            }
        }
    }

    protected open fun onCreateSelectionTracker(videoFiles: ArrayList<VideoFile>) {}

    companion object {
        const val READ_PERMISSION_CODE = 1
        const val READ_PERMISSION_DIALOG_TAG = "Read Permission Tag"
    }
}