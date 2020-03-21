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
import java.util.Comparator;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public final class FindMeetingQuery {
  public Collection<TimeRange> query(Collection<Event> events, MeetingRequest request) {
    Collection<String> attendees = request.getAttendees();
    long duration = request.getDuration();
    boolean inattendance = false; 
    int entireDay = 1440;
    
    List<Event> newEvents = new ArrayList<Event>();

    if (duration > entireDay)
    {
        return Arrays.asList();
    }

    for (Event event : events) {
        for (String attendee : attendees) { 
            if (event.getAttendees().contains(attendee))
            {
                newEvents.add(event); 
                break; 
            }
        }
    }

    newEvents.sort(Comparator.comparing(Event::getWhen, TimeRange.ORDER_BY_START.thenComparing(TimeRange.ORDER_BY_END)));
    List<TimeRange> results = new ArrayList<TimeRange> ();

    if (newEvents.size()==0)
    {
        results.add(TimeRange.WHOLE_DAY);
    }

    if (newEvents.size()==1)
    {
        if (newEvents.get(0).getWhen().start()!=0)
        {
            results.add(TimeRange.fromStartDuration(0, newEvents.get(0).getWhen().start()));
        }
        if (newEvents.get(0).getWhen().end()!=entireDay)
        {
            results.add(TimeRange.fromStartDuration(newEvents.get(0).getWhen().end(), entireDay - newEvents.get(0).getWhen().end()));
        }
    }

    for (int i = 0; i < newEvents.size()-1; i++)
    { 
        TimeRange currentTimeRange = newEvents.get(i).getWhen(); 
        TimeRange nextTimeRange = newEvents.get(i+1).getWhen(); 
        if (i==0 && currentTimeRange.start()!=0)
        {
            results.add(TimeRange.fromStartDuration(0, currentTimeRange.start()));
        }
        if (currentTimeRange.overlaps(nextTimeRange))
        {
            if (nextTimeRange.end()<=currentTimeRange.end())
            {
                newEvents.set(i+1, new Event("hi", currentTimeRange, new ArrayList<>())); 
                nextTimeRange = currentTimeRange; 
            }
            else 
            {
                newEvents.set(i+1, new Event("hello", TimeRange.fromStartDuration(currentTimeRange.start(), nextTimeRange.end()-currentTimeRange.start()), 
                new ArrayList<>()));
                nextTimeRange = TimeRange.fromStartDuration(currentTimeRange.start(), nextTimeRange.end()-currentTimeRange.start());
            }
        }
        else 
        {
            results.add(TimeRange.fromStartDuration(currentTimeRange.end(), nextTimeRange.start() - currentTimeRange.end())); 
        }

        if (i == newEvents.size()-2 && nextTimeRange.end()!=entireDay)
        {
            results.add(TimeRange.fromStartDuration(nextTimeRange.end(), entireDay - nextTimeRange.end()));
        }
    }

    return results; 
  }
}
