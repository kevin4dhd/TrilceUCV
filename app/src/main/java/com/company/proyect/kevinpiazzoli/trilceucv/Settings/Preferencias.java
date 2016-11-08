package com.company.proyect.kevinpiazzoli.trilceucv.Settings;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.support.v4.app.FragmentManager;

import com.company.proyect.kevinpiazzoli.trilceucv.Settings.Dialogs.ColorChooserDialog;
import com.company.proyect.kevinpiazzoli.trilceucv.R;
import com.github.machinarius.preferencefragment.PreferenceFragment;

/**
 * Created by Kevin Piazzoli on 12/10/2016.
 */

public class Preferencias extends PreferenceFragment{

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferencias);

        final ListPreference listPreference = (ListPreference) findPreference("key_tabla");
        final Preference listPreference2 = findPreference("key_theme");
        setListPreferenceData(listPreference);

        listPreference2.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                FragmentManager fragmentManager = getFragmentManager();
                ColorChooserDialog dialog = new ColorChooserDialog();
                dialog.show(fragmentManager, "fragment_color_chooser");
                return false;
            }
        });

        listPreference.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                setListPreferenceData(listPreference);
                return false;
            }
        });
    }

    protected static void setListPreferenceData(ListPreference lp) {
        CharSequence[] entries = { "Estilo Basico", "Estilo Items", "Estilo Items 2", "Estilo Items 3"};
        CharSequence[] entryValues = {"1" , "2", "3", "4"};
        lp.setEntries(entries);
        lp.setDefaultValue("1");
        lp.setEntryValues(entryValues);
    }
}
