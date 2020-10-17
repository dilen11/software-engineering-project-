package edu.uark.registerapp.controllers;

import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import edu.uark.registerapp.commands.exceptions.UnauthorizedException;
import edu.uark.registerapp.commands.transaction.TransactionCreateCommand;
import edu.uark.registerapp.commands.transaction.TransactionDeleteCommand;
import edu.uark.registerapp.commands.transaction.TransactionEntryCreateCommand;
import edu.uark.registerapp.controllers.enums.QueryParameterNames;
import edu.uark.registerapp.controllers.enums.ViewNames;
import edu.uark.registerapp.models.api.ApiResponse;
import edu.uark.registerapp.models.api.Product;
import edu.uark.registerapp.models.api.TransactionEntryCreate;
import edu.uark.registerapp.models.entities.ActiveUserEntity;

@RestController
@RequestMapping(value = "/api/transaction")
public class TransactionRestController extends BaseRestController {
	@RequestMapping(value = "/", method = RequestMethod.POST)
	public @ResponseBody ApiResponse createTransaction(
		@RequestBody final Product product,
		final HttpServletRequest request,
		final HttpServletResponse response
	) {

		try {
			final ActiveUserEntity activeUserEntity =
				this.validateActiveUserCommand
					.setSessionKey(request.getSession().getId())
					.execute();

			if (activeUserEntity == null) {
				return this.redirectSessionNotActive(response);
			}

			final UUID transactionId = this.transactionCreateCommand
				.setCashierId(activeUserEntity.getEmployeeId())
				.execute();

			response.setStatus(HttpStatus.CREATED.value());
			return (new ApiResponse())
				.setRedirectUrl(
					ViewNames.PRODUCT_SEARCH.getViewName().concat(
						this.buildInitialQueryParameter(
							QueryParameterNames.TRANSACTION_ID.getValue(),
							transactionId.toString())));
		} catch (final UnauthorizedException e) {
			return this.redirectSessionNotActive(response);
		} catch (final Exception e) {
			response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());

			return (new ApiResponse())
				.setErrorMessage("Unable to create transaction. " + e.getMessage());
		}
	}

	@RequestMapping(value = "/entry/", method = RequestMethod.POST)
	public @ResponseBody ApiResponse updateProduct(
		@RequestBody final TransactionEntryCreate transactionEntryCreate,
		final HttpServletRequest request,
		final HttpServletResponse response
	) {

		this.transactionEntryCreateCommand
			.setCreateData(transactionEntryCreate)
			.execute();

		return (new ApiResponse())
			.setRedirectUrl("/transactionSummary/"
				+ transactionEntryCreate.getTransactionId().toString());
	}

	@RequestMapping(value = "{transactionId}", method = RequestMethod.DELETE)
	public @ResponseBody ApiResponse deleteTransaction(
			@PathVariable final UUID transactionId,
			final HttpServletRequest request,
			final HttpServletResponse response
	) {
		this.transactionDeleteCommand.
			setTransactionId(transactionId).execute();
			
		return new ApiResponse();
	}

	// Properties
	@Autowired
	private TransactionCreateCommand transactionCreateCommand;

	@Autowired
	private TransactionEntryCreateCommand transactionEntryCreateCommand;

	@Autowired
	private TransactionDeleteCommand transactionDeleteCommand;
}