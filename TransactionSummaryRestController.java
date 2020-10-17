package edu.uark.registerapp.controllers;

import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import edu.uark.registerapp.models.api.ApiResponse;
import edu.uark.registerapp.models.api.TransactionEntryCreate;

public class TransactionSummaryRestController extends BaseRestController {
    //Update the quantity for a transactionentry record.
    @RequestMapping(value = "/{transactionId}", method = RequestMethod.PATCH)
    public @ResponseBody ApiResponse updateTransaction(
    @RequestBody final TransactionEntryCreate transactionEntry,
    @PathVariable final UUID transactionId,
    final HttpServletRequest request,
    final HttpServletResponse response) {
        return null;
        
    }
    //Remove a transactionentry record
    @RequestMapping(value = "/{transactionId}", method = RequestMethod.DELETE)
    public @ResponseBody ApiResponse removeTransaction(
    @PathVariable final UUID transactionId,
    final HttpServletRequest request,
    final HttpServletResponse response) {
        return null;
        
    } 

}