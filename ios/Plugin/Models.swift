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
