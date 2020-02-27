//
//  Models.swift
//  Plugin
//
//  Created by Christopher Woolum on 2/27/20.
//  Copyright © 2020 Max Lynch. All rights reserved.
//

import Foundation

class Contact {
    var name: String;
    var id: String;
    var emails: [String];
    var phoneNumbers: [String];
    
    init(
        name: String,
        id: String,
        emails: [String],
        phoneNumbers: [String]) {
        self.name = name
        self.id = id
        self.phoneNumbers = phoneNumbers
        self.emails = emails
    }
}


extension Contact : Serializable {
    var properties: Array<String> {
        return ["name", "id", "emails", "phoneNumbers"]
    }

    func valueForKey(key: String) -> Any? {
        switch key {
        case "name":
            return name
        case "id":
            return id
        case "emails":
            return emails
        case "phoneNumbers":
            return phoneNumbers
        default:
            return nil
        }
    }
}
