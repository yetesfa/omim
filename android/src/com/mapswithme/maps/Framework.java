package com.mapswithme.maps;

import android.os.Build;

import com.mapswithme.maps.MapStorage.Index;
import com.mapswithme.maps.bookmarks.data.DistanceAndAzimut;
import com.mapswithme.maps.bookmarks.data.MapObject;
import com.mapswithme.maps.bookmarks.data.MapObject.SearchResult;
import com.mapswithme.maps.guides.GuideInfo;
import com.mapswithme.util.Constants;
import com.mapswithme.util.Utils;

/**
 * This class wraps android::Framework.cpp class
 * via static methods
 */
public class Framework
{
  public interface OnBalloonListener
  {
    public void onApiPointActivated(double lat, double lon, String name, String id);

    public void onPoiActivated(String name, String type, String address, double lat, double lon);

    public void onBookmarkActivated(int category, int bookmarkIndex);

    public void onMyPositionActivated(double lat, double lon);

    public void onAdditionalLayerActivated(String name, String type, double lat, double lon);

    public void onDismiss();
  }

  public interface RoutingListener
  {
    void onRoutingError(String messageId);
  }

  // this class is just bridge between Java and C++ worlds, we must not create it
  private Framework()
  {}

  public static String getHttpGe0Url(double lat, double lon, double zoomLevel, String name)
  {
    return nativeGetGe0Url(lat, lon, zoomLevel, name).replaceFirst(Constants.Url.GE0_PREFIX, Constants.Url.HTTP_GE0_PREFIX);
  }

  public static native void nativeShowTrackRect(int category, int track);

  // guides
  public native static GuideInfo getGuideInfoForIndex(Index idx);

  public static GuideInfo getGuideInfoForIndexWithApiCheck(Index idx)
  {
    if (Utils.apiLowerThan(Build.VERSION_CODES.HONEYCOMB))
      return null;
    else
      return getGuideInfoForIndex(idx);
  }

  public static GuideInfo getGuideInfoForIdWithApiCheck(String id)
  {
    if (Utils.apiLowerThan(Build.VERSION_CODES.HONEYCOMB))
      return null;
    else
      return getGuideById(id);
  }

  public native static void setWasAdvertised(String appId);

  public native static boolean wasAdvertised(String appId);

  public native static String[] getGuideIds();

  public native static GuideInfo getGuideById(String id);

  public native static int getDrawScale();

  public native static double[] getScreenRectCenter();

  public native static DistanceAndAzimut nativeGetDistanceAndAzimut(double merX, double merY, double cLat, double cLon, double north);

  public native static DistanceAndAzimut nativeGetDistanceAndAzimutFromLatLon(double lat, double lon, double cLat, double cLon, double north);

  public native static String nativeFormatLatLon(double lat, double lon, boolean useDMSFormat);

  public native static String[] nativeFormatLatLonToArr(double lat, double lon, boolean useDMSFormat);

  public native static String nativeFormatAltitude(double alt);

  public native static String nativeFormatSpeed(double speed);

  public native static String nativeGetGe0Url(double lat, double lon, double zoomLevel, String name);

  public native static String nativeGetNameAndAddress4Point(double lat, double lon);

  public native static MapObject nativeGetMapObjectForPoint(double lat, double lon);

  public native static void nativeActivateUserMark(double lat, double lon);

  public native static void nativeConnectBalloonListeners(OnBalloonListener listener);

  public native static void nativeClearBalloonListeners();

  public native static String nativeGetOutdatedCountriesString();

  public native static boolean nativeIsDataVersionChanged();

  public native static void nativeUpdateSavedDataVersion();

  public native static void nativeClearApiPoints();

  public native static void injectData(SearchResult searchResult, long index);

  public native static void cleanSearchLayerOnMap();

  public native static void invalidate();

  public native static String[] nativeGetMovableFilesExt();

  public native static String nativeGetBookmarksExt();

  public native static String nativeGetBookmarkDir();

  public native static String nativeGetSettingsDir();

  public native static String nativeGetWritableDir();

  public native static void nativeSetWritableDir(String newPath);

  public native static void nativeLoadbookmarks();

  /// @name Routing.
  //@{
  public native static boolean nativeIsRoutingActive();

  public native static void nativeCloseRouting();

  public native static void nativeBuildRoute(double lat, double lon);

  public native static void nativeFollowRoute();

  public native static LocationState.RoutingInfo nativeGetRouteFollowingInfo();

  public native static void nativeAddRoutingListener(RoutingListener listener);
  //@}

  public native static String nativeGetCountryNameIfAbsent(double lat, double lon);

  public native static Index nativeGetCountryIndex(double lat, double lon);

  public native static String nativeGetViewportCountryNameIfAbsent();

  public native static void nativeShowCountry(Index idx, boolean zoomToDownloadButton);
}
