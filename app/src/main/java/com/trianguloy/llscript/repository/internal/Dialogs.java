package com.trianguloy.llscript.repository.internal;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListAdapter;

import com.trianguloy.llscript.repository.Constants;
import com.trianguloy.llscript.repository.IntentHandle;
import com.trianguloy.llscript.repository.R;

/**
 * Created by Lukas on 28.04.2015.
 * Holds Methods to show Dialogs used by more than one Activity.
 * May hold all Dialogs in future
 */
public final class Dialogs {
    private Dialogs(){}

    public static void badLogin(Context context){
        badLogin(context,null);
    }

    public static void badLogin(Context context, @Nullable DialogInterface.OnClickListener onClose) {
        new AlertDialog.Builder(context)
                .setTitle(context.getString(R.string.title_error))
                .setMessage(context.getString(R.string.text_badLogin))
                .setNeutralButton(R.string.button_ok, onClose)
                .show();
    }

    public static void connectionFailed(Context context){
        connectionFailed(context, null);
    }

    public static void connectionFailed(Context context,@ Nullable DialogInterface.OnClickListener onClose) {
        new AlertDialog.Builder(context)
                .setTitle(context.getString(R.string.title_error))
                .setMessage(context.getString(R.string.text_cantConnect))
                .setNeutralButton(R.string.button_ok, onClose)
                .show();
    }

    public static void pageAlreadyExists(Context context) {
        new AlertDialog.Builder(context)
                .setTitle(context.getString(R.string.title_error))
                .setMessage(context.getString(R.string.text_alreadyExists))
                .setNeutralButton(R.string.button_ok, null)
                .show();
    }

    public static void saved(final Activity context, final String savedPageId){
        new AlertDialog.Builder(context)
                .setTitle(context.getString(R.string.title_saved))
                .setMessage(context.getString(R.string.text_doNext))
                .setPositiveButton(context.getString(R.string.button_viewPage), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(Intent.ACTION_VIEW);
                        intent.setData(Uri.parse(context.getString(R.string.link_scriptPagePrefix) + savedPageId.substring(context.getString(R.string.prefix_script).length())));
                        intent.setClass(context, IntentHandle.class);
                        intent.putExtra(Constants.extraReload, true);
                        context.startActivity(intent);
                        context.finish();
                    }
                })
                .setNeutralButton(context.getString(R.string.button_goHome), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(Intent.ACTION_VIEW);
                        intent.setData(Uri.parse(context.getString(R.string.link_repository)));
                        intent.setClass(context, IntentHandle.class);
                        intent.putExtra(Constants.extraReload, true);
                        context.startActivity(intent);
                        context.finish();
                    }
                })
                .setNegativeButton(context.getString(R.string.button_stay), null)
                .show();
    }

    public static void cantSaveEmpty(Context context){
        new AlertDialog.Builder(context)
                .setTitle(context.getString(R.string.title_error))
                .setMessage(context.getString(R.string.text_cantSaveEmpty))
                .setNeutralButton(context.getString(R.string.button_ok), null)
                .show();
    }

    public static void unsavedChanges(Context context, @Nullable DialogInterface.OnClickListener onConfirm){
        new AlertDialog.Builder(context)
                .setTitle(context.getString(R.string.title_warning))
                .setMessage(context.getString(R.string.text_unsavedChanges))
                .setPositiveButton(context.getString(R.string.button_yes), onConfirm)
                .setNegativeButton(context.getString(R.string.button_no), null)
                .show();
    }

    public static void selectPageToEdit(Context context, ListAdapter adapter, DialogInterface.OnClickListener listener){
        new AlertDialog.Builder(context)
                .setAdapter(adapter, listener)
                .setNegativeButton(R.string.button_cancel, null)
                .setTitle(R.string.title_selectPage)
                .show();
    }

    public static void removeSubscription(Context context,String which, DialogInterface.OnClickListener onConfirm){
        new AlertDialog.Builder(context)
                .setTitle(R.string.title_remove)
                .setMessage(context.getString(R.string.message_remove) + which + context.getString(R.string.text_questionmark))
                .setPositiveButton(R.string.button_ok, onConfirm)
                .setNegativeButton(R.string.button_cancel, null)
                .show();
    }

    public static void launcherNotFound(final Activity context){
        new AlertDialog.Builder(context)
                .setCancelable(false)
                .setTitle(R.string.title_warning)
                .setMessage(R.string.message_launcherNotFound)
                .setNeutralButton(R.string.button_ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent i = new Intent(Intent.ACTION_VIEW);
                        i.setData(Uri.parse(context.getString(R.string.link_playStorePrefix) + Constants.packages[1]));
                        context.startActivity(i);
                        context.finish();
                    }
                })
                .setIcon(R.drawable.ic_launcher)
                .show();
    }

    public static void launcherOutdated(final Activity context){
        new AlertDialog.Builder(context)
                .setCancelable(false)
                .setTitle(R.string.title_warning)
                .setMessage(R.string.message_outdatedLauncher)
                .setNeutralButton(R.string.button_ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent i = new Intent(Intent.ACTION_VIEW);
                        i.setData(Uri.parse(context.getString(R.string.link_playStorePrefix) + Constants.installedPackage));
                        context.startActivity(i);
                        context.finish();
                    }
                })
                .setIcon(R.drawable.ic_launcher)
                .show();
    }

    public static void changedSubscriptions(Context context,String changed){
        new AlertDialog.Builder(context)
                .setTitle(R.string.title_updatedSubs)
                .setMessage(changed)
                .setNeutralButton(R.string.button_ok, null)
                .show();
    }

    public static void importScript(Activity context, final String code, String scriptName, final OnImportListener onImport, final OnImportListener onShare){

        View layout = context.getLayoutInflater().inflate(R.layout.confirm_alert, (ViewGroup) context.findViewById(R.id.webView).getRootView(), false);
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB) layout.setBackgroundColor(Color.WHITE);
        final EditText contentText = ((EditText) layout.findViewById(R.id.editText2));
        contentText.setText(code);
        final EditText nameText = ((EditText) layout.findViewById(R.id.editText));
        nameText.setText(scriptName);
        final CheckBox[] flagsBoxes = {
                (CheckBox) layout.findViewById(R.id.checkBox1),
                (CheckBox) layout.findViewById(R.id.checkBox2),
                (CheckBox) layout.findViewById(R.id.checkBox3)};

        new AlertDialog.Builder(context)
                .setTitle(R.string.title_importer)
                .setIcon(R.drawable.ic_launcher)
                .setView(layout)
                .setPositiveButton(R.string.button_import, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        final int flags = (flagsBoxes[0].isChecked() ? Constants.FLAG_APP_MENU : 0) +
                                (flagsBoxes[1].isChecked() ? Constants.FLAG_ITEM_MENU : 0) +
                                (flagsBoxes[2].isChecked() ? Constants.FLAG_CUSTOM_MENU : 0);
                        onImport.onClick(contentText.getText().toString(), nameText.getText().toString(), flags);
                    }
                })
                .setNeutralButton(R.string.button_share, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        final int flags = (flagsBoxes[0].isChecked() ? Constants.FLAG_APP_MENU : 0) +
                                (flagsBoxes[1].isChecked() ? Constants.FLAG_ITEM_MENU : 0) +
                                (flagsBoxes[2].isChecked() ? Constants.FLAG_CUSTOM_MENU : 0);
                        onShare.onClick(contentText.getText().toString(), nameText.getText().toString(), flags);
                    }
                })
                .setNegativeButton(R.string.button_exit, null)
                .show();
    }

    public interface OnImportListener{
        void onClick(String code, String name, int flags);
    }

    public static void moreThanOneScriptFound(Context context,final String[] names,DialogInterface.OnClickListener onSelect){
        new AlertDialog.Builder(context)
                .setTitle(R.string.title_severalScriptsFound)
                .setIcon(R.drawable.ic_launcher)
                .setSingleChoiceItems(names, android.R.layout.simple_list_item_single_choice, onSelect)
                .setNegativeButton(R.string.button_cancel, null)
                .show();
    }

    public static void noPageLoaded(final Activity context,DialogInterface.OnClickListener onRetry){
        new AlertDialog.Builder(context)
                .setTitle(R.string.title_noPageFound)
                .setMessage(R.string.message_noPageFound)
                .setPositiveButton(R.string.button_retry, onRetry)
                .setNegativeButton(R.string.button_exit, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        context.finish();
                    }
                })
                .setIcon(R.drawable.ic_launcher)
                .setCancelable(false)
                .show();
    }

    public static void noScriptFound(final Context context){
        new AlertDialog.Builder(context)
                .setTitle(R.string.title_importer)
                .setNegativeButton(R.string.button_exit, null)
                .setPositiveButton(R.string.text_googlePlus, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Intent j = new Intent(Intent.ACTION_VIEW);
                        j.setData(Uri.parse(context.getString(R.string.link_playStoreImporter)));
                        context.startActivity(j);
                    }
                })
                .setIcon(R.drawable.ic_launcher)
                .setMessage(R.string.message_noScriptFound)
                .show();
    }

    public static void subscribe(Context context,DialogInterface.OnClickListener onConfirm){
        new AlertDialog.Builder(context)
                .setTitle(R.string.title_subscribe)
                .setMessage(R.string.message_subscribeAsk)
                .setNegativeButton(R.string.button_cancel, null)
                .setPositiveButton(R.string.button_ok, onConfirm)
                .show();
    }
}
