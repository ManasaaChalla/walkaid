package com.example.walkaid.utils;


import android.util.Log;

import com.example.walkaid.TimeConverter;
import com.slt.lib.datamanager.DataManager;
import com.slt.lib.running.RunningReport;


import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;


public class RunningResultPresenter {

    private RunningResultListener mListener;
    private Locale mLocale;

    private int mRunningDistanceUnit = 0;
    private long mRunningId;
    private RunningReport mRunningReport;
    private DataManager mDataManager;


    public RunningResultPresenter(long runningId, DataManager dataManager, RunningResultListener listener, Locale locale, int distanceUnit) {
        mRunningId = runningId;
        mDataManager = dataManager;
        mListener = listener;
        mLocale = locale;
        mRunningDistanceUnit = distanceUnit;
        checkData();
    }

    private void checkData() {
        mDataManager.getRunningReport(mRunningId, runningReport -> {
            mRunningReport = runningReport;
            if (mRunningReport != null) {
                Log.v("UB-Report", runningReport.toString());
                Date date = mRunningReport.getStartDate();
                DateFormat formatDay = DateFormat.getDateInstance(DateFormat.MEDIUM);
                SimpleDateFormat format = new SimpleDateFormat("a h:mm", mLocale);
                String dateStr = formatDay.format(date) + " " + format.format(date);
                mListener.setDateTitle(dateStr);
                setData();
            }

            return null;
        });


    }

    private void setData() {
        String distance = mRunningReport.getDistance() > 0 ? getDistance(mRunningReport.getDistance()) : "0.00";
        String duration = String.valueOf(mRunningReport.getDuration() > 0 ? new TimeConverter(mRunningReport.getDuration()).getFullTime() : "00:00");
        String calorie = String.valueOf(mRunningReport.getCalorie() > 0 ? mRunningReport.getCalorie() : "0");
        String step = String.valueOf(mRunningReport.getSteps() > 0 ? mRunningReport.getSteps() : "0");


        mListener.setDuration(duration);
        mListener.setDistance(distance);
        mListener.setCalorie(calorie);
        mListener.setSteps(step);

        int fore = mRunningReport.getLandingForePercent();
        int mid = mRunningReport.getLandingMidPercent();
        int heel = mRunningReport.getLandingHeelPercent();

        mListener.setAnalysisForeFootStrikeSub(String.valueOf(fore));
        mListener.setAnalysisMidFootStrikeSub(String.valueOf(mid));
        mListener.setAnalysisHeelFootStrikeSub(String.valueOf(heel));

        /*
           type = 1 <- 포어풋
           type = 2 <- 미드풋
           type = 3 <- 힐풋
         */
        String footPercent;
        if (fore == 100 || mid == 100 || heel == 100) {
            if (fore == 100) {
                footPercent = String.valueOf(fore);
                mListener.setAnalysisFootStrikeMain(1, footPercent);
                mListener.setForeFootIndicator();
            } else if (mid == 100) {
                footPercent = String.valueOf(mid);
                mListener.setAnalysisFootStrikeMain(2, footPercent);
                mListener.setMidFootIndicator();
            } else {
                footPercent = String.valueOf(heel);
                mListener.setAnalysisFootStrikeMain(3, footPercent);
                mListener.setHeelFootIndicator();
            }
        } else {
            if (fore == mid && mid == heel) {
                // 세개가 모두 같을때
            } else {
                if (fore >= mid) {
                    if (fore == mid) {
                        if (fore > heel) {
                            footPercent = String.valueOf(fore);
                            mListener.setAnalysisFootStrikeMain(1, footPercent);
                            mListener.setForeFootIndicator();
                            // 두번째 MidFoot
                        } else {
                            footPercent = String.valueOf(heel);
                            mListener.setAnalysisFootStrikeMain(3, footPercent);
                            mListener.setHeelFootIndicator();
                            // 두번째 ForeFoot
                        }
                    } else {
                        // Fore > Mid
                        if (fore >= heel) {
                            footPercent = String.valueOf(fore);
                            mListener.setAnalysisFootStrikeMain(1, footPercent);
                            mListener.setForeFootIndicator();

                            if (mid >= heel) {
                                // 두번재 Mid
                            } else {
                                // 두번재 Heel
                            }
                        } else {
                            // Heel > Fore > Mid
                            footPercent = String.valueOf(heel);
                            mListener.setAnalysisFootStrikeMain(3, footPercent);
                            mListener.setHeelFootIndicator();
                            // 두번째 Fore
                        }
                    }
                } else {
                    // Mid > Fore 이다.
                    if (mid >= heel) {
                        footPercent = String.valueOf(mid);
                        mListener.setAnalysisFootStrikeMain(2, footPercent);
                        mListener.setMidFootIndicator();

                        if (heel == mid) {
                            // 두번째 Heel
                        }

                    } else {
                        // Heel > Mid
                        footPercent = String.valueOf(heel);
                        mListener.setAnalysisFootStrikeMain(3, footPercent);
                        mListener.setHeelFootIndicator();
                    }
                }
            }
        }

        String paceStrMile = "" + mRunningReport.getPace();
        if (mRunningDistanceUnit == 1) {
            double paceUnit = mRunningReport.getDuration() / (mRunningReport.getDistance() / 1609.34);
            paceStrMile = String.valueOf(paceUnit);
        }
        mListener.setPace("" + mRunningReport.getPace(), paceStrMile);
        mListener.setCadence(mRunningReport.getCadence());
        mListener.setGroundContactTime(mRunningReport.getGctValue());
    }

    private String getDistance(double value) {
        int distance = (int) value;
        if (mRunningDistanceUnit == 1) {
            double tmp = value / 1609.34;
            return String.format(Locale.US, "%.2f", tmp);
        }

        StringBuilder sb = new StringBuilder();
        distance = distance / 10;

        if (distance >= 100) {
            int tempFront = distance / 100;
            sb.append(tempFront);
            sb.append(".");
            int tempBack = distance - tempFront * 100;

            if (tempBack >= 10) {
                sb.append(tempBack);
            } else {
                sb.append("0");
                sb.append(tempBack);
            }
        } else {
            if (distance >= 10) {
                sb.append("0.");
                sb.append(distance);
            } else {
                sb.append("0.0");
                sb.append(distance);
            }
        }

        return sb.toString();
    }

    public void onClickRoute() {
        /*mListener.onClickRoute(mRunningId);*/
    }

}
