package com.example.petproject.presentation.editNote.camera

import android.content.Context
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.widget.LinearLayout
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.view.LifecycleCameraController
import androidx.camera.view.PreviewView
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.core.net.toFile
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.compose.rememberAsyncImagePainter
import com.example.petproject.R
import com.example.petproject.presentation.model.NoteUi
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CameraScreen(
    cameraViewModel: CameraViewModel = hiltViewModel(),
    onBack: (String) -> Unit,
    noteTitle: String?,
    coroutineScope: CoroutineScope
) {

    val uiState = cameraViewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(coroutineScope) {
        if (noteTitle != null) {
            launch {
                cameraViewModel.updateTitle(noteTitle)
            }
        }
    }

    val context = LocalContext.current
    val cameraController = remember { LifecycleCameraController(context) }
    val lifecycleOwner = LocalLifecycleOwner.current
    val lensFacing = remember { mutableStateOf(CameraSelector.LENS_FACING_BACK) }
    val imageTakenUri = remember { mutableStateOf(Uri.EMPTY) }
    val flashMode = remember { mutableStateOf(ImageCapture.FLASH_MODE_OFF) }
    val cameraSelector = remember(lensFacing.value) {
        CameraSelector.Builder().requireLensFacing(lensFacing.value).build()
    }

    Scaffold(modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(title = { Text(text = uiState.value.noteTitle) })
        },
        bottomBar = {
            Row(
                modifier = Modifier.fillMaxWidth(),
            ) {
                if (imageTakenUri.value == Uri.EMPTY) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(
                                color = Color.Black
                            )
                            .padding(30.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
//                        Image(
//                            contentDescription = if (flashMode.value == ImageCapture.FLASH_MODE_OFF) "flashlight off" else "flashlight on",
//                            modifier = Modifier
//                                .width(25.dp)
//                                .height(30.dp)
//                                .clickable {
//                                    if (flashMode.value == ImageCapture.FLASH_MODE_ON) flashMode.value =
//                                        ImageCapture.FLASH_MODE_OFF
//                                    else flashMode.value = ImageCapture.FLASH_MODE_ON
//                                    cameraController.imageCaptureFlashMode = flashMode.value
//                                },
//                            contentScale = ContentScale.FillBounds
//                        )
                        Image(
                            imageVector = ImageVector.vectorResource(id = R.drawable.photo_button),
                            contentDescription = "take photo",
                            modifier = Modifier
                                .size(62.dp)
                                .clickable {
                                    takePhoto(
                                        cameraController = cameraController,
                                        context = context,
                                        onImageCaptured = {
                                            imageTakenUri.value = it
//                                            cameraViewModel.savePhotoPath(it.toString())
                                        }
                                    )
                                },
                            contentScale = ContentScale.FillBounds
                        )
                        Image(
                            imageVector = ImageVector.vectorResource(id = R.drawable.switch_camera_24dp_e8eaed_fill0_wght400_grad0_opsz24),
                            contentDescription = "switch camera",
                            modifier = Modifier
                                .width(32.dp)
                                .height(30.dp)
                                .clickable {
                                    if (lensFacing.value == CameraSelector.LENS_FACING_FRONT) {
                                        lensFacing.value = CameraSelector.LENS_FACING_BACK
                                    } else {
                                        lensFacing.value = CameraSelector.LENS_FACING_FRONT
                                    }
                                },
                            contentScale = ContentScale.FillBounds
                        )
                    }
                } else {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 25.dp),
                        horizontalArrangement = Arrangement.SpaceAround,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(70.dp)
                                    .clip(shape = RoundedCornerShape(10.dp)),
                                contentAlignment = Alignment.Center
                            ) {
                                Image(
                                    imageVector = ImageVector.vectorResource(id = R.drawable.refresh_24dp_e8eaed_fill0_wght400_grad0_opsz24),
                                    contentDescription = "refresh",
                                    modifier = Modifier
                                        .width(30.dp)
                                        .height(30.dp)
                                        .clickable {
                                            if (imageTakenUri.value != Uri.EMPTY) {
                                                imageTakenUri.value
                                                    .toFile()
                                                    .delete()
                                                imageTakenUri.value = Uri.EMPTY
//                                                cameraViewModel.savePhotoPath("")
                                            }
                                        },
                                    contentScale = ContentScale.FillBounds
                                )
                            }
                            Text(
                                text = "Retake",
                                modifier = Modifier.padding(top = 8.dp)
                            )
                        }
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(70.dp)
                                    .background(
                                        color = Color.Transparent
                                    ),
                                contentAlignment = Alignment.Center
                            ) {
                                Image(
                                    imageVector = ImageVector.vectorResource(id = R.drawable.check_24dp_e8eaed_fill0_wght400_grad0_opsz24),
                                    contentDescription = "done",
                                    colorFilter = ColorFilter.tint(color = Color.White),
                                    modifier = Modifier
                                        .size(23.dp)
                                        .clickable {
                                            val uri = imageTakenUri.value
                                            if (uri != null) {
//                                                cameraViewModel.updateNote(uri.toString())
                                                onBack(uri.toString())
                                            }
                                        },
                                    contentScale = ContentScale.FillBounds
                                )
                            }
                            Text(
                                text = "Save & continue",
                                modifier = Modifier.padding(top = 8.dp)
                            )
                        }
                    }
                }
            }
        }) { innerPadding ->
        Column(modifier = Modifier
            .fillMaxSize()
            .padding(innerPadding)) {
            if (imageTakenUri.value != Uri.EMPTY) {
                Image(
                    painter = rememberAsyncImagePainter(
                        model  = Uri.parse(imageTakenUri.value.toString())  // or ht
                    ),
                    contentDescription = "takenPhoto",
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.FillBounds
                )
//                AsyncImage(
//                    model = ImageRequest.Builder(LocalContext.current)
//                        .data(imageTakenUri)
//                        .build(),
//                    contentDescription = "takenPhoto",
//                    modifier = Modifier.fillMaxSize(),
//                    contentScale = ContentScale.FillBounds
//                    )
            } else {
                AndroidView(
                    modifier = Modifier
                        .fillMaxSize(),
                    factory = { context ->
                        PreviewView(context).apply {
                            layoutParams = LinearLayout.LayoutParams(MATCH_PARENT, MATCH_PARENT)
                            setBackgroundColor(Color.Black.toArgb())
                            scaleType = PreviewView.ScaleType.FILL_START
                            implementationMode = PreviewView.ImplementationMode.COMPATIBLE
                        }.also { previewView ->
                            previewView.controller = cameraController
                            cameraController.cameraSelector = cameraSelector
                            cameraController.imageCaptureFlashMode = flashMode.value
                            cameraController.bindToLifecycle(lifecycleOwner)
                        }
                    },
                    onRelease = {
                        cameraController.unbind()
                    },
                    onReset = {},
                    update = {
                        cameraController.cameraSelector = cameraSelector
                    }
                )
            }
        }
    }
}

private fun takePhoto(
    cameraController: LifecycleCameraController,
    context: Context,
    onImageCaptured: (uri: Uri) -> Unit
) {

    val imageFile = File.createTempFile(
        SimpleDateFormat(
            "yyyy-MM-dd-HH-mm-ss-SSS",
            Locale.US
        ).format(System.currentTimeMillis()), ".jpg"
    )

    val outputFileOptions = ImageCapture.OutputFileOptions.Builder(imageFile).build()
    val mainExecutor = ContextCompat.getMainExecutor(context)
    cameraController.takePicture(outputFileOptions, mainExecutor, object :
        ImageCapture.OnImageSavedCallback {
        override fun onImageSaved(outputFileResults: ImageCapture.OutputFileResults) {
            val savedUri = Uri.fromFile(imageFile)
            val file = savedUri.toFile()
            onImageCaptured(savedUri)
        }
        override fun onError(exception: ImageCaptureException) {
//            Timber.d("exception arises on saving a captured image in temp file")
        }
    })
}
fun getImageFileInPngFormat(uri: Uri, context: Context): File {
    var bitmap: Bitmap? = null
    if (Build.VERSION.SDK_INT < 28) {
        bitmap = MediaStore.Images.Media.getBitmap(context.contentResolver, uri)
    } else {
        val source = ImageDecoder.createSource(context.contentResolver, uri)
        bitmap = ImageDecoder.decodeBitmap(source)
    }
    val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(
        Date()
    )
    val storageDir: File? = context.getExternalFilesDir(null)
    val file = File.createTempFile(
        "PNG_${timeStamp}_",
        ".png",
        storageDir
    )
    FileOutputStream(file).use { out ->
        bitmap?.compress(Bitmap.CompressFormat.JPEG, 100, out)
        out.flush()
    }
    return file
}