package br.com.matteroftime.core.listeners;

/**
 * Created by Valentine on 2/24/2016.
 */
public interface OnDatabaseOperationCompleteListener {
    void onSQLOperationFailed(String error);
    void onSQLOperationSucceded(String message);
}
