package com.lockedIn.lockedIn_backend.service;

import com.lockedIn.lockedIn_backend.dto.ReportResponse;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EmailTemplateService {

    public String buildReportHtml(ReportResponse response) {

        return """
            <!DOCTYPE html>
            <html>
            <head>
                <meta charset="UTF-8">
                <style>
                    body{
                        margin:0;
                        padding:20px;
                        background:#f4f7fb;
                        font-family:Arial,sans-serif;
                    }

                    .container{
                        max-width:700px;
                        margin:auto;
                        background:white;
                        border-radius:16px;
                        overflow:hidden;
                        box-shadow:0 4px 20px rgba(0,0,0,0.1);
                    }

                    .header{
                        background:linear-gradient(135deg,#111827,#2563eb);
                        color:white;
                        text-align:center;
                        padding:30px;
                    }

                    .header h1{
                        margin:0;
                    }

                    .header p{
                        opacity:0.9;
                    }

                    .section{
                        padding:25px;
                    }

                    .card{
                        background:#f8fafc;
                        border-radius:12px;
                        padding:18px;
                        margin-bottom:15px;
                        border-left:6px solid #2563eb;
                    }

                    .study{
                        border-left-color:#22c55e;
                    }

                    .distraction{
                        border-left-color:#ef4444;
                    }

                    .title{
                        font-size:14px;
                        color:#6b7280;
                        margin-bottom:5px;
                    }

                    .value{
                        font-size:22px;
                        font-weight:bold;
                        color:#111827;
                    }

                    .list{
                        margin-top:10px;
                        padding-left:20px;
                    }

                    .footer{
                        text-align:center;
                        padding:20px;
                        color:#6b7280;
                        font-size:12px;
                        background:#f9fafb;
                    }

                </style>
            </head>

            <body>

            <div class="container">

                <div class="header">
                    <h1>🔥 LockedIn Daily Report</h1>
                    <p>Your focus summary for today</p>
                </div>

                <div class="section">

                    <div class="card study">
                        <div class="title">Study Time</div>
                        <div class="value">%s</div>
                    </div>

                    <div class="card distraction">
                        <div class="title">Distraction Time</div>
                        <div class="value">%s</div>
                    </div>

                    <div class="card">
                        <div class="title">Distraction Count</div>
                        <div class="value">%d</div>
                    </div>

                    <div class="card">
                        <div class="title">Completed Tasks</div>
                        <ul class="list">
                            %s
                        </ul>
                    </div>

                    <div class="card">
                        <div class="title">Remaining Tasks</div>
                        <ul class="list">
                            %s
                        </ul>
                    </div>

                    <div class="card distraction">
                        <div class="title">Top Distractions</div>
                        <ul class="list">
                            %s
                        </ul>
                    </div>

                </div>

                <div class="footer">
                    LockedIn • Stop scrolling. Start building.
                </div>

            </div>

            </body>
            </html>
            """.formatted(
                response.getStudyTime(),
                response.getDistractionTime(),
                response.getDistractionCount(),
                convertToHtmlList(response.getCompletedTask()),
                convertToHtmlList(response.getRemainingTask()),
                convertToHtmlList(response.getTopDistractions())
        );
    }

    private String convertToHtmlList(List<String> items){
        if(items == null || items.isEmpty()){
            return "<li>None</li>";
        }

        StringBuilder html = new StringBuilder();

        for(String item : items){
            html.append("<li>").append(item).append("</li>");
        }

        return html.toString();
    }
}

