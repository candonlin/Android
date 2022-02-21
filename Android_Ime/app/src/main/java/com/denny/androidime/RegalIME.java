package com.denny.androidime;

import android.app.Service;
import android.inputmethodservice.InputMethodService;
import android.inputmethodservice.Keyboard;
import android.inputmethodservice.KeyboardView;
import android.media.AudioManager;
import android.os.Vibrator;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputConnection;
import android.view.inputmethod.InputMethodManager;

import static android.view.inputmethod.EditorInfo.IME_ACTION_DONE;
import static android.view.inputmethod.EditorInfo.IME_ACTION_GO;
import static android.view.inputmethod.EditorInfo.IME_ACTION_NEXT;
import static android.view.inputmethod.EditorInfo.IME_ACTION_NONE;
import static android.view.inputmethod.EditorInfo.IME_ACTION_SEARCH;
import static android.view.inputmethod.EditorInfo.IME_ACTION_SEND;

public class RegalIME extends InputMethodService implements KeyboardView.OnKeyboardActionListener {
    private InputMethodManager m_inputMethodManager;
    private KeyboardView keyboardView;      // 對應keyboard.xml中定义的KeyboardView
    private Keyboard qwertyKeyboard;        // 字母鍵盤
    private Keyboard numericKeyboard;       // 数字鍵盤
    private boolean isNum = false;          // 是否數字鍵盤
    private boolean isUpper = false;        // 是否大寫
    private Vibrator myVibrator;

    private int mCurKeyboard;

    @Override
    public void onCreate() {
        super.onCreate();
//        android.os.Debug.waitForDebugger();
        Log.d(this.getClass().toString(), "onCreate: ");

        m_inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        myVibrator = (Vibrator) getApplication().getSystemService(Service.VIBRATOR_SERVICE);
    }

    @Override
    public void setInputView(View view) {
        Log.d("SimpleIME", "<<<<setInputView");
        super.setInputView(view);
        switch (mCurKeyboard) {
            case InputType.TYPE_CLASS_TEXT:
                keyboardView.setKeyboard(qwertyKeyboard);
                break;

            case InputType.TYPE_CLASS_NUMBER:
                keyboardView.setKeyboard(numericKeyboard);
                break;

            default:
                keyboardView.setKeyboard(numericKeyboard);
//                keyboardView.setKeyboard(qwertyKeyboard);
                break;
        }
    }

//    @Override
//    public boolean onShowInputRequested(int flags, boolean configChange) {
////        return super.onShowInputRequested(flags, configChange);
//        return true;
//    }

    @Override
    public View onCreateInputView() {
        Log.d(this.getClass().toString(), "onCreateInputView: ");
        // keyboard被创建后，将调用onCreateInputView函数
        keyboardView = (KeyboardView) getLayoutInflater().inflate(R.layout.keyboard, null);      //使用了keyboard.xml
        qwertyKeyboard = new Keyboard(this, R.xml.qwerty);                                    //使用了qwerty.xml
        numericKeyboard = new Keyboard(this, R.xml.number_keyboard2);                         //使用了qwerty.xml
//        keyboardView.setKeyboard(qwertyKeyboard);
        keyboardView.setKeyboard(numericKeyboard);
        keyboardView.setOnKeyboardActionListener(this);

        return keyboardView;
    }

    @Override
    public void onStartInput(EditorInfo editorInfo, boolean restarting) {
        super.onStartInput(editorInfo, restarting);
        mCurKeyboard = editorInfo.inputType & InputType.TYPE_MASK_CLASS;
    }

    @Override
    public void onStartInputView(EditorInfo info, boolean restarting) {
        super.onStartInputView(info, restarting);
        switch (mCurKeyboard) {
            case InputType.TYPE_CLASS_NUMBER:
                isNum = true;
                keyboardView.setKeyboard(numericKeyboard);
                break;
            case InputType.TYPE_CLASS_TEXT:
                isNum = false;
                keyboardView.setKeyboard(qwertyKeyboard);
            default:
                keyboardView.setKeyboard(numericKeyboard);
//                keyboardView.setKeyboard(qwertyKeyboard);
        }

        // 如果Editetext  沒有imeOptions 會自動判斷是否有下一個焦點 來決定 imeOptions 為 Next or Done
        CharSequence action_label = getTextByImeAction(getCurrentInputEditorInfo().imeOptions);
        if (isNum) {
            numericKeyboard.getKeys().get(19).label = action_label;
        } else {
            qwertyKeyboard.getKeys().get(33).label = action_label;
        }
        keyboardView.invalidateAllKeys();
    }

    @Override
    public void onPress(int i) {

    }

    @Override
    public void onRelease(int i) {

    }

    @Override
    public void onKey(int primaryCode, int[] keyCodes) {
        InputConnection ic = getCurrentInputConnection();
        playClick(primaryCode);
        switch (primaryCode) {
            case Keyboard.KEYCODE_DELETE:
//                ic.deleteSurroundingText(1, 0);
                CharSequence selectedText = ic.getSelectedText(0);
                if (TextUtils.isEmpty(selectedText)) {
                    ic.deleteSurroundingText(1, 0);
                } else {
                    // delete the selection
                    ic.commitText("", 1);
                }
                break;
            case Keyboard.KEYCODE_DONE:
                Log.d("onKey", "" + primaryCode);

                int imeOptions = getCurrentInputEditorInfo().imeOptions;
                switch (imeOptions & EditorInfo.IME_MASK_ACTION) {
                    case IME_ACTION_NONE:
                        ic.performEditorAction(IME_ACTION_NONE);
                        break;
                    case IME_ACTION_GO:
                        ic.performEditorAction(IME_ACTION_GO);
                        break;
                    case EditorInfo.IME_ACTION_SEARCH:
                        ic.performEditorAction(IME_ACTION_SEARCH);
                        break;
                    case IME_ACTION_SEND:
                        ic.performEditorAction(IME_ACTION_SEND);
                        break;
                    case IME_ACTION_NEXT:
                        ic.performEditorAction(IME_ACTION_NEXT);
                        break;
                    case EditorInfo.IME_ACTION_DONE:
                        ic.performEditorAction(IME_ACTION_DONE);
                        break;
                    default:
                        ic.sendKeyEvent(new KeyEvent(KeyEvent.ACTION_UP, KeyEvent.KEYCODE_ENTER));
                        ic.sendKeyEvent(new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_ENTER));
                        break;
                }
                break;
            case Keyboard.KEYCODE_MODE_CHANGE:
                if (isNum) {
                    isNum = false;
                    keyboardView.setKeyboard(qwertyKeyboard);
                } else {
                    isNum = true;
                    keyboardView.setKeyboard(numericKeyboard);
                }
                break;
            case Keyboard.KEYCODE_SHIFT:
                isUpper = !isUpper;
                Keyboard.Key currentKey = qwertyKeyboard.getKeys().get(qwertyKeyboard.getShiftKeyIndex());
                if (isUpper) {
                    currentKey.icon = getResources().getDrawable(R.drawable.icons8_down_24);
                } else {
                    currentKey.icon = getResources().getDrawable(R.drawable.icons8_upper_24);
                }
                qwertyKeyboard.setShifted(isUpper);
                keyboardView.invalidateAllKeys();
                break;
            default:
                char code = (char) primaryCode;
                if (code == ' ') {
                    ic.commitText(" ", 1);
                } else {
                    String str = Character.toString((char) primaryCode);
                    if (isUpper) {
                        str = str.toUpperCase();
                    } else {
                        str = str.toLowerCase();
                    }
                    ic.commitText(str, 1);
                }

        }
    }


    @Override
    public void onText(CharSequence charSequence) {

    }

    @Override
    public void swipeLeft() {

    }

    @Override
    public void swipeRight() {

    }

    @Override
    public void swipeDown() {

    }

    @Override
    public void swipeUp() {

    }

    /**
     * 點擊按鍵時撥放聲音
     *
     * @param keyCode
     */
    private void playClick(int keyCode) {
        AudioManager am = (AudioManager) getSystemService(AUDIO_SERVICE);
        myVibrator.vibrate(100);
        switch (keyCode) {
            case 32:
                am.playSoundEffect(AudioManager.FX_KEYPRESS_SPACEBAR);
                break;
            case Keyboard.KEYCODE_DONE:
            case 10:
                am.playSoundEffect(AudioManager.FX_KEYPRESS_RETURN);
                break;
            case Keyboard.KEYCODE_DELETE:
                am.playSoundEffect(AudioManager.FX_KEYPRESS_DELETE);
                break;
            default:
                am.playSoundEffect(AudioManager.FX_KEYPRESS_STANDARD);
        }
    }


    /**
     * 獲取Action Text
     *
     * @param imeOptions
     * @return
     */
    public CharSequence getTextByImeAction(int imeOptions) {
        switch (imeOptions & EditorInfo.IME_MASK_ACTION) {
            case EditorInfo.IME_ACTION_NONE:
                return null;
            case EditorInfo.IME_ACTION_GO:
                return getText(R.string.ime_action_go);
            case EditorInfo.IME_ACTION_SEARCH:
                return getText(R.string.ime_action_search);
            case EditorInfo.IME_ACTION_SEND:
                return getText(R.string.ime_action_send);
            case EditorInfo.IME_ACTION_NEXT:
                return getText(R.string.ime_action_next);
            case EditorInfo.IME_ACTION_DONE:
                return getText(R.string.ime_action_done);
            case EditorInfo.IME_ACTION_PREVIOUS:
                return getText(R.string.ime_action_previous);
            default:
                return getText(R.string.ime_action_default);
        }
    }

}
