package com.liudonghan.multi_image.permission;

/**
 * Description：
 *
 * @author Created by: Li_Min
 * Time:1/9/23
 */
public class ManifestRegisterException extends RuntimeException {

    ManifestRegisterException(String permission) {
        super(permission == null ?
                "No permissions are registered in the manifest file" :
                (permission + ": Permissions are not registered in the manifest file"));
    }
}