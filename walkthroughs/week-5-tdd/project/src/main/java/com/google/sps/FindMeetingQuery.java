// Copyright 2019 Google LLC
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     https://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package com.google.sps;

import java.util.Collection;
import java.util.*;

public final class FindMeetingQuery {
  public Collection<TimeRange> query(Collection<Event> events, MeetingRequest request) {

    Collection<TimeRange> availableTimeRange = new ArrayList<>();
    Collection<String> requestAttendees = request.getAttendees();
    ArrayList<TimeRange> knownEventTimeRange = new ArrayList<>();

    int startOfDay = TimeRange.START_OF_DAY;
    int endOfDay = TimeRange.END_OF_DAY;
    long durationOfRequestMeeting = request.getDuration();

    if(durationOfRequestMeeting > TimeRange.WHOLE_DAY.duration()){

        return availableTimeRange;

    } else if (events.size() == 0){

        TimeRange totalTime = TimeRange.fromStartEnd(startOfDay, endOfDay, true);
        availableTimeRange.add(totalTime);
        return availableTimeRange;

    }

    
    for(Event event : events){
        Set<String> attendees = event.getAttendees();

        //Check if every attendee in each event will be attending the requested meeting
        int countAttendees = 0;
        for(String attendee : attendees){
            if(requestAttendees.contains(attendee)){
                countAttendees++;
            }
        }
        if(countAttendees == attendees.size()){
            knownEventTimeRange.add(event.getWhen());
        }
    }

    if(knownEventTimeRange.size() == 0){
        TimeRange totalTime = TimeRange.fromStartEnd(startOfDay, endOfDay, true);
        availableTimeRange.add(totalTime);
        return availableTimeRange;
    }

    Collections.sort(knownEventTimeRange, TimeRange.ORDER_BY_START);

    int numberOfKnownEvents = knownEventTimeRange.size();
    TimeRange lastEventRange = knownEventTimeRange.get(numberOfKnownEvents - 1);
    for(int i = 0; i < knownEventTimeRange.size(); i++){
        TimeRange currentEventTimeRange = knownEventTimeRange.get(i);
        if(i == 0){
            if(startOfDay != currentEventTimeRange.start()){

                if(durationOfRequestMeeting < (currentEventTimeRange.start() - startOfDay)){
                    TimeRange currentRange = TimeRange.fromStartEnd(startOfDay, currentEventTimeRange.start(), false);
                    availableTimeRange.add(currentRange);
                }
            }
        } else if(i == knownEventTimeRange.size() - 1){
            TimeRange previousEventTimeRange = knownEventTimeRange.get(i - 1);

            if(previousEventTimeRange.contains(currentEventTimeRange)){
                lastEventRange = previousEventTimeRange;

            } else{
                lastEventRange = currentEventTimeRange;
                if(previousEventTimeRange.end() != currentEventTimeRange.start()){
                TimeRange currentRange = TimeRange.fromStartEnd(previousEventTimeRange.end(), currentEventTimeRange.start(), false);
                
                // This makes sure that the duration of the created TimeRange is greater than the duration of the requested meeting.
                if(currentRange.duration() + 1 > (durationOfRequestMeeting)){
                    availableTimeRange.add(currentRange);
                }
            }
            }
        }
        else{

            // The previous time range is included to get the beginning of an available time range for the requested meeting.
            TimeRange previousEventTimeRange = knownEventTimeRange.get(i - 1);
            if(previousEventTimeRange.end() != currentEventTimeRange.start()){
                TimeRange currentRange = TimeRange.fromStartEnd(previousEventTimeRange.end(), currentEventTimeRange.start(), false);
                
                // This makes sure that the duration of the created TimeRange is greater than the duration of the requested meeting.
                if(currentRange.duration() + 1 > (durationOfRequestMeeting)){
                    availableTimeRange.add(currentRange);
                }
            }
        }
    }

    // A piece of code to take care of the time range between the last known event and the end of the day.
    if(lastEventRange.end() < endOfDay){
        
        TimeRange currentRange = TimeRange.fromStartEnd(lastEventRange.end(), endOfDay, true);
        availableTimeRange.add(currentRange);
    
    }

    return availableTimeRange;
    
  }
    
}
